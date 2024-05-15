package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
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
public class GetMeetingDetailRes {
    private String title;

    private Long userId;

    private Long categoryId;

    private String content;

    private String address;

    private String addressDetail;

    private String buildingName;

    private Integer maxParticipantsCount;

    private Integer participantsCount;

    private List<MeetingParticipantListMapper> participantList;

    private String image;

    private String appointmentTime;

    private LocalDateTime createdAt;

    public static GetMeetingDetailRes of(Meeting meeting, List<MeetingParticipantListMapper> participantList) {
        return GetMeetingDetailRes.builder()
                .title(meeting.getTitle())
                .userId(meeting.getUserId())
                .categoryId(meeting.getCategoryId())
                .content(meeting.getContent())
                .address(meeting.getAddress())
                .addressDetail(meeting.getAddressDetail())
                .buildingName(meeting.getBuildingName())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .participantsCount(meeting.getParticipantsCount())
                .participantList(participantList)
                .image(meeting.getImage())
                .appointmentTime(meeting.getAppointmentTime())
                .createdAt(meeting.getCreateAt())
                .build();
    }
}