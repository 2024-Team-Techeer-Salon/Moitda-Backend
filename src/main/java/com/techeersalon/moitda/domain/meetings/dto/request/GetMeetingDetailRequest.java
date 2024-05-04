package com.techeersalon.moitda.domain.meetings.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMeetingDetailRequest {

    private Long meetingId;

}
