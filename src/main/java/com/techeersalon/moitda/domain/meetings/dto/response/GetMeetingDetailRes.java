package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
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

    private String roadAddressName;

    private String DetailedAddress;

    private String placeName;

    private Integer maxParticipantsCount;

    private Integer participantsCount;

    private List<MeetingParticipantListMapper> participantList;

    private List<MeetingImage> imageList;

    private String appointmentTime;

    private LocalDateTime createdAt;

    private Boolean isOwner;

    public static GetMeetingDetailRes of(Meeting meeting, List<MeetingParticipantListMapper> participantList, List<MeetingImage> imageList) {
        return GetMeetingDetailRes.builder()
                .title(meeting.getTitle())
                .userId(meeting.getUserId())
                .categoryId(meeting.getCategoryId())
                .content(meeting.getContent())
                .roadAddressName(meeting.getAddress())
                .DetailedAddress(meeting.getAddressDetail())
                .placeName(meeting.getBuildingName())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .participantsCount(meeting.getParticipantsCount())
                .participantList(participantList)
                .imageList(imageList)
                .appointmentTime(meeting.getAppointmentTime())
                .createdAt(meeting.getCreateAt())
                .build();
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }
}