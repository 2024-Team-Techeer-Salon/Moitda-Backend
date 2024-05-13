package com.techeersalon.moitda.domain.meetings.dto.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantMapper {
    private Long id;
    private Long meetingId;
    private Long userId;
}
