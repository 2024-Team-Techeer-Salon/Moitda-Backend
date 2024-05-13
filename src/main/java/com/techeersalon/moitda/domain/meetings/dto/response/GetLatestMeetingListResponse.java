package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetLatestMeetingListResponse {
    private Long meetingId;

    private Long userId;

    private String title;

    private String imageUrl;

    private String address;

    private Integer participantsCount;

    private Integer maxParticipantsCount;

    public static GetLatestMeetingListResponse of(Meeting meeting){

        return GetLatestMeetingListResponse.builder()
                .meetingId(meeting.getId())
                .userId(meeting.getUserId())
                .title(meeting.getTitle())
                .imageUrl(meeting.getImage())
                .address(meeting.getAddress())
                .participantsCount(meeting.getParticipantsCount())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .build();
    }
}
