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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetMeetingDetailResponse {
    private String title;

    private Long userId;

    private Long categoryId;

    private String content;

    private Integer maxParticipantsCount;

    private Integer participantsCount;

    private List<MeetingParticipant> participantList;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static GetMeetingDetailResponse of(Meeting meeting, List<MeetingParticipant> participantList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return GetMeetingDetailResponse.builder()
                .title(meeting.getTitle())
                .userId(meeting.getUserId())
                .categoryId(meeting.getCategoryId())
                .content(meeting.getContent())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .participantsCount(meeting.getParticipantsCount())
                .participantList(participantList)
                .imageUrl(meeting.getImage())
                .createdAt(meeting.getCreateAt())
                .build();
    }
}