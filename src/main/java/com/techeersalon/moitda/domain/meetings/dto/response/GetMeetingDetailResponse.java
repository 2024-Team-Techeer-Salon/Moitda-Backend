package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetMeetingDetailResponse {
    private String title;

    private Long user_id;

    private Long catagory_id;

    private String content;

    private Integer max_participants_count;

    private Integer participants_count;

    private List<MeetingParticipant> participantList;

    private String image_url;

    private LocalDateTime createdAt;

    public static GetMeetingDetailResponse of(Meeting meeting, List<MeetingParticipant> participantList) {

        return GetMeetingDetailResponse.builder()
                .title(meeting.getTitle())
                .user_id(meeting.getUserId())
                .catagory_id(meeting.getCategoryId())
                .content(meeting.getContent())
                .max_participants_count(meeting.getMaxParticipantsCount())
                .participants_count(meeting.getParticipantsCount())
                .participantList(participantList)
                .image_url(meeting.getImage())
                .createdAt(LocalDateTime.parse(meeting.getCreateAt()))
                .build();
    }
}