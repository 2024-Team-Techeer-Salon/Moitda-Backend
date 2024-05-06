
package com.techeersalon.moitda.domain.meetings.service;

import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingRequest;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingResponse;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailResponse;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public void addParticipantOfMeeting(Long meetingId, Long participantId) {

    }

    public void addMeeting(CreateMeetingRequest dto) {
        Meeting entity = dto.toEntity();
        meetingRepository.save(entity);
    }

    public GetMeetingDetailResponse findMeetingById(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);

        return GetMeetingDetailResponse.of(meeting);
    }

    public Meeting getMeetingById(Long id) {
        // 아직 예외처리는 안함
        return meetingRepository.findById(id).orElse(null);
    }
}