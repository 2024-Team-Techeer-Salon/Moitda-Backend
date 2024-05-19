package com.techeersalon.moitda.domain.meetings.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantMapper {
    private Long meetingId;
    private Long userId;
    private String username;

    public static MeetingParticipant toEntity(Meeting meeting) {
        return MeetingParticipant.builder()
                .meetingId(meeting.getId())
                .userId(meeting.getUserId())
                .username(meeting.getUsername())
                .isWaiting(Boolean.TRUE)
                .build();
    }

    public static MeetingParticipant toEntity(Meeting meeting, User user) {
        return MeetingParticipant.builder()
                .meetingId(meeting.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .isWaiting(Boolean.TRUE)
                .build();
    }
}
