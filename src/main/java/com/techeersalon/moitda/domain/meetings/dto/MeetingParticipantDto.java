package com.techeersalon.moitda.domain.meetings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantDto {
    private Long id;
    private Long meetingId;
    private Long userId;
}
