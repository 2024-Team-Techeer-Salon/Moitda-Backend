
package com.techeersalon.moitda.domain.meetings.service;

import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantMapper;
import com.techeersalon.moitda.domain.meetings.dto.request.ApprovalParticipantReq;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingRes;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateParticipantRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetLatestMeetingListRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailRes;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingIsFullException;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingPageNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.participant.AlreadyParticipatingOrAppliedException;
import com.techeersalon.moitda.domain.meetings.exception.participant.MeetingParticipantNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.participant.NotAuthorizedToAppproveException;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingService {
    @Autowired
    private final MeetingRepository meetingRepository;
    @Autowired
    private final MeetingParticipantRepository meetingParticipantRepository;
    @Autowired
    private final UserService userService;


    // 한 페이지당 meeting 데이터 개수
    private final int pageSize = 32;

    /*
     * 미팅 생성 메소드
     * Meeting, MeetingParticipant 생성
     * Meeting에 참가자 1명 추가
     * */
    public CreateMeetingRes createMeeting(CreateMeetingReq dto) {
        User loginUser = userService.getLoginUser();
        Meeting entity = dto.toEntity(loginUser);

        // 최대 참가 인원 유효성 검사
        entity.validateMaxParticipantsCount();
        Meeting meeting = meetingRepository.save(entity);

        MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);
        // 미팅에 바로 참여
        participant.notNeedToApprove();
        meetingParticipantRepository.save(participant);

        return CreateMeetingRes.from(meeting.getId());
    }
    /*
     * 미팅 상세 조회 메소드
     * 미팅 데이터 값 중 필요한 내용과 미팅 참가자 리스트 reponse에 담음
     * */
    public GetMeetingDetailRes findMeetingById(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);

        Optional<MeetingParticipant> participantOptional = meetingParticipantRepository.findByMeetingIdAndIsWaiting(meetingId, Boolean.FALSE);
        participantOptional.orElseThrow(MeetingParticipantNotFoundException::new);

        List<MeetingParticipantListMapper> participantDtoList = participantOptional
                .stream()
                .map(MeetingParticipantListMapper::from)
                .collect(Collectors.toList());
        return GetMeetingDetailRes.of(meeting, participantDtoList);
    }

//    private MeetingParticipantMapper mapToDto(MeetingParticipant meetingParticipant) {
//
//        return new MeetingParticipantMapper(
//                meetingParticipant.getId()
//        );
//    }

    /*
     * 참가자 신청 메소드
     * 미팅에 선착순인 경우와 승인이 필요한 경우
     * */
    public CreateParticipantRes addParticipantOfMeeting(Long meetingId) {
        User loginUser = userService.getLoginUser();
        Long loginUserId = loginUser.getId();
        Meeting meeting = null;

        // 이미 유저가 미팅에 참가자 거나 참가 신청을 했을 경우 예외처리
        if (meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUserId)) {
            throw new AlreadyParticipatingOrAppliedException();
        }
        //log.info(String.valueOf(meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUserId)));
        meeting = this.getMeetingById(meetingId);

        // 미팅 참가자가 이미 최대 참가자수를 넘을 경우 예외처리
        if (meeting.getParticipantsCount() >= meeting.getMaxParticipantsCount()){
            throw new MeetingIsFullException();
        }

        MeetingParticipant entity = MeetingParticipantMapper.toEntity(meeting,loginUserId);

        // 미팅이 선착순일 경우 바로 미팅 참가자로 변경
        if (!meeting.getApprovalRequired()) {
            entity.notNeedToApprove();
            meeting.increaseParticipantsCnt();
        }

        MeetingParticipant participant = meetingParticipantRepository.save(entity);

        return CreateParticipantRes.from(participant.getId());
//        if (!meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUser.getId())) {
//            meeting = this.getMeetingById(meetingId);
//            if (meeting.getParticipantsCount() < meeting.getMaxParticipantsCount()) {
//
//                MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);
//
//                if (!meeting.getApprovalRequired()) {
//                    participant.notNeedToApprove();
//                    meeting.increaseParticipantsCnt();
//                }
//
//                meetingParticipantRepository.save(participant);
//
//            } else {
//                throw new MeetingIsFullException();
//            }
//
//        } else {
//            throw new AlreadyParticipatingOrAppliedException();
//        }
    }

    public void approvalParticipant(ApprovalParticipantReq dto) {
        // 참가자 존재 예외처리
        MeetingParticipant participant = meetingParticipantRepository.findById(dto.getParticipantId()).orElseThrow(MeetingParticipantNotFoundException::new);
        // 승인한 미팅이 참가자 미팅이 다른 경우 예외처리
        if(participant.getMeetingId().longValue() != dto.getMeetingId().longValue()){
            throw new NotAuthorizedToAppproveException();
        }
        participant.notNeedToApprove();

        if (dto.getIsApproval()) { // 승인 할 경우
            Meeting meeting = this.getMeetingById(participant.getMeetingId());
            meeting.increaseParticipantsCnt();
            meetingParticipantRepository.save(participant);
        } else { // 거절 할 경우
            meetingParticipantRepository.delete(participant);
            //participant.delete();
        }
    }

//    public List<Meeting> getUserMeetingList(){
//        Long loginUserId = userService.getLoginUser().getId();
//        return meetingRepository.findByUserId(loginUserId);
//    }
    /*
     * 미팅 리스트 조회 메소드
     * 간략화 된 미팅 내용을 최대 32개인 한 페이지로 준다.
     * */
    public List<GetLatestMeetingListRes> findMeetings(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createAt"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Page<Meeting> meetings = meetingRepository.findAll(pageable);
        if (meetings.isEmpty()) {
            throw new MeetingPageNotFoundException();
        }

        return GetLatestMeetingListRes.listOf(meetings).getContent();
    }
    /*
     * 미팅 삭제 메소드
     * 미팅, 미팅의 참가자 모두 softDelete
     * */
    public void deleteMeeting(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        Optional<MeetingParticipant> participantOptional = meetingParticipantRepository.findByMeetingId(meetingId);
        participantOptional.orElseThrow(MeetingParticipantNotFoundException::new);

        meetingRepository.delete(meeting);
        //meetingParticipantRepository.save(participant);
        meetingParticipantRepository.deleteAll(participantOptional.stream().toList());
        //meetingRepository.save(meeting);
    }

    private Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
    }
    /*
     * 미팅 수정 메소드
     * 미팅 전체 내용 수정
     * */
    public void modifyMeeting(Long meetingId, ChangeMeetingInfoReq dto) {
        Meeting meeting = this.getMeetingById(meetingId);
        meeting.updateInfo(dto);
        meetingRepository.save(meeting);
    }

    public void endMeeting(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        meeting.updateEndTime(LocalDateTime.now().toString());
    }
}