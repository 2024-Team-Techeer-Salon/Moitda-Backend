package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import com.techeersalon.moitda.domain.user.entity.User;
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

    private String username;

    private String profileImage;

    private Integer mannerStat;

    private Long categoryId;

    private String content;

    private String roadAddressName;

    private String detailedAddress;

    private String placeName;

    private Integer maxParticipantsCount;

    private Integer participantsCount;

    private List<MeetingParticipantListMapper> participantList;

    private List<MeetingParticipantListMapper> waitingList;

    private List<MeetingImage> imageList;

    private String appointmentTime;

    private LocalDateTime createdAt;

    private Boolean isOwner;

    private String endTime;

    private Boolean approvalRequired;

    private Boolean participantValid;

    // meeting table에 userId, username을 저장할 필요가 있나요?
    public static GetMeetingDetailRes of(Meeting meeting, User user, List<MeetingParticipantListMapper> participantList, List<MeetingParticipantListMapper> waitingList, List<MeetingImage> imageList, Boolean participantValid) {
        return GetMeetingDetailRes.builder()
                .title(meeting.getTitle())
                .userId(meeting.getUserId())    // 필요없으면 주석된 부분 바꿔야함.
                .username(meeting.getUsername())    //
                .profileImage(user.getProfileImage())
                .mannerStat(user.getMannerStat())
                .categoryId(meeting.getCategoryId())
                .content(meeting.getContent())
                .roadAddressName(meeting.getRoadAddressName())
                .detailedAddress(meeting.getDetailedAddress())
                .placeName(meeting.getPlaceName())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .participantsCount(meeting.getParticipantsCount())
                .participantList(participantList)
                .waitingList(waitingList)
                .imageList(imageList)
                .appointmentTime(meeting.getAppointmentTime())
                .createdAt(meeting.getCreateAt())
                .endTime(meeting.getEndTime())
                .approvalRequired(meeting.getApprovalRequired())
                .participantValid(participantValid)
                .build();
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }
}