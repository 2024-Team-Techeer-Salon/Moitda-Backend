package com.techeersalon.moitda.domain.meetings.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingResponse {
    private Long meeting_id;

    public static CreateMeetingResponse from(){
        return CreateMeetingResponse.builder().build();
    }
}