
package com.techeersalon.moitda.domain.meetings.service;

import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantMapper;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.response.GetLatestMeetingListRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailRes;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
    public Long createMeeting(CreateMeetingReq dto) {
        User loginUser = userService.getLoginUser();

        Meeting entity = dto.toEntity(loginUser);
        Meeting meeting = meetingRepository.save(entity);

        MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);
        participant.notNeedToApprove();
        meetingParticipantRepository.save(participant);

        return meeting.getId();
    }

    public GetMeetingDetailRes findMeetingById(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        List<MeetingParticipantListMapper> participantDtoList = meetingParticipantRepository.findByMeetingIdAndIsWaiting(meetingId, Boolean.FALSE)
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

    public void addParticipantOfMeeting(Long meetingId) {
        User loginUser = userService.getLoginUser();
        Meeting meeting;
        if (!meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUser.getId())) {
            Optional<Meeting> meetingOptional = meetingRepository.findById(meetingId);

            if (meetingOptional.isPresent()) {
                meeting = meetingOptional.get();

                if (meeting.getParticipantsCount() < meeting.getMaxParticipantsCount()) {

                    MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);

                    if (!meeting.getApprovalRequired()) {
                        participant.notNeedToApprove();
                        meeting.increaseParticipantsCnt();
                    }

                    meetingParticipantRepository.save(participant);

                } else {
                    throw new IllegalStateException("가득참.");
                }
            } else {
                throw new IllegalStateException("존재하지 않은 미팅");
            }
        } else {
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    public void approvalParticipant(Long participantId, Boolean isApproval) {
        MeetingParticipant participant = meetingParticipantRepository.findById(participantId).orElse(null);
        participant.notNeedToApprove();
        if (isApproval) {
            Meeting meeting = getMeetingById(participant.getMeetingId());
            meeting.increaseParticipantsCnt();
        } else {
            meetingParticipantRepository.delete(participant);
            //participant.delete();
        }
        //meetingParticipantRepository.save(participant);
    }

//    public List<Meeting> getUserMeetingList(){
//        Long loginUserId = userService.getLoginUser().getId();
//        return meetingRepository.findByUserId(loginUserId);
//    }

    public Page<GetLatestMeetingListRes> findMeetings(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createAt"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Page<Meeting> meetings = meetingRepository.findAll(pageable);

        return meetings.map(GetLatestMeetingListRes::from);
    }

    public void deleteMeeting(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        List<MeetingParticipant> participantList = meetingParticipantRepository.findByMeetingId(meetingId);
        meetingRepository.delete(meeting);
        //meetingParticipantRepository.save(participant);
        meetingParticipantRepository.deleteAll(participantList);
        //meetingRepository.save(meeting);
    }

    private Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 모임이 존재하지 않습니다."));
    }

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