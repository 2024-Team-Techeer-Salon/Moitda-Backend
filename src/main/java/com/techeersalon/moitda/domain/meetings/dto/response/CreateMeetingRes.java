package com.techeersalon.moitda.domain.meetings.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRes {
    private Long meetingId;

    public static CreateMeetingRes from(Long meetingId){
        return CreateMeetingRes.builder()
                .meetingId(meetingId)
                .build();
    }
}
