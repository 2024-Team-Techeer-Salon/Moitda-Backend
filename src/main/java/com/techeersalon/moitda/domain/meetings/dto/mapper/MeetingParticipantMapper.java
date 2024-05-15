package com.techeersalon.moitda.domain.meetings.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantMapper {
    private Long meetingId;
    private Long userId;

    public static MeetingParticipant toEntity(Meeting meeting) {
        return MeetingParticipant.builder()
                .meetingId(meeting.getId())
                .userId(meeting.getUserId())
                .isWaiting(Boolean.TRUE)
                .build();
    }

    public static MeetingParticipant toEntity(Meeting meeting, Long userId) {
        return MeetingParticipant.builder()
                .meetingId(meeting.getId())
                .userId(userId)
                .isWaiting(Boolean.TRUE)
                .build();
    }
}
