
package com.techeersalon.moitda.domain.meetings.service;

import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingRequest;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailResponse;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final UserService userService;

    public Long addMeeting(CreateMeetingRequest dto) {
        User loginUser = userService.getLoginUser();

        Meeting entity = dto.toEntity(loginUser);
        Meeting meeting = meetingRepository.save(entity);

        MeetingParticipant participant = new MeetingParticipant(meeting.getId(), loginUser.getId());
        participant.setIsWaiting(false);
        meetingParticipantRepository.save(participant);

        return meeting.getId();
    }

    public GetMeetingDetailResponse findMeetingById(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        List<MeetingParticipant> participantList = meetingParticipantRepository.findByMeetingIdAndIsWaiting(meetingId, Boolean.FALSE);
//        filter
        return GetMeetingDetailResponse.of(meeting, participantList);
    }

    public Meeting getMeetingById(Long id) {
        // 아직 예외처리는 안함
        return meetingRepository.findById(id).orElse(null);
    }

    public void addParticipantOfMeeting(Long meetingId) {
        User loginUser = userService.getLoginUser();
        if (!meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUser.getId())) {
            Optional<Meeting> meetingOptional = meetingRepository.findById(meetingId);

            if (meetingOptional.isPresent()) {
                Meeting meeting = meetingOptional.get();

                if (meeting.getParticipantsCount() < meeting.getMaxParticipantsCount()) {

                    MeetingParticipant participant = new MeetingParticipant(meetingId, loginUser.getId());

                    if (meeting.getApprovalRequired() != true) {
                        participant.notNeedToApprove();
                        // 인원 수 추가 해야할듯..
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

    public void approvalParticipant(Long userIdOfParticipant, Boolean isApproval) {
        MeetingParticipant participant = meetingParticipantRepository.findById(userIdOfParticipant).orElse(null);
        if (isApproval) {
            participant.setIsWaiting(false);
        } else {
            participant.delete();
        }
        meetingParticipantRepository.save(participant);
    }
//  이 부분이 뭔지 잘 몰라서 안썼는데 이 메서드가 사용되야 한다고 하면 말씀해주세요
//    public List<Meeting> getUserMeetingList(){
//        Long loginUserId = userService.getLoginUser().getId();
//        return meetingRepository.findByUserId(loginUserId);
//    }
}