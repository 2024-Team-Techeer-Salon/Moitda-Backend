package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.dto.MeetingParticipantDto;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String address;

    private String addressDetail;

    private String buildingName;

    private Integer maxParticipantsCount;

    private Integer participantsCount;

    private List<MeetingParticipantDto> participantList;

    private String image;

    private String appointmentTime;

    private String createdAt;

    public static GetMeetingDetailResponse of(Meeting meeting, List<MeetingParticipantDto> participantList) {
        return GetMeetingDetailResponse.builder()
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
                .createdAt(meeting.getCreateAt().toString())
                .build();
    }
}