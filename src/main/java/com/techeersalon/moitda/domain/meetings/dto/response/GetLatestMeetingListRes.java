package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetLatestMeetingListRes {
    private Long meetingId;

    private Long userId;

    private String title;

    private List<MeetingImage> imageUrl;

    private String address;

    private Integer participantsCount;

    private Integer maxParticipantsCount;

    public static GetLatestMeetingListRes from(Meeting meeting, List<MeetingImage> images){

        return GetLatestMeetingListRes.builder()
                .meetingId(meeting.getId())
                .userId(meeting.getUserId())
                .title(meeting.getTitle())
                .imageUrl(images)
                .address(meeting.getAddress())
                .participantsCount(meeting.getParticipantsCount())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .build();
    }

}
