package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
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
public class GetLatestMeetingListRes {

    private Long meetingId;

    private Long categoryId;

    private String username;

    private String title;

    private String imageUrl;

    private String roadAddressName;

    private String placeName;

    private String detailedAddress;

    private Integer participantsCount;

    private Integer maxParticipantsCount;

    private Double latitude;

    private Double longitude;

    private String appointmentTime;

    private String endTime;


    public static GetLatestMeetingListRes from(Meeting meeting, List<MeetingImage> images) {
        String[] roadAddress = meeting.getRoadAddressName().split(" ");
        String roadAddressName, url;
        // 앞에 두 단어만 roadAddressName으로 설정
        try {
            roadAddressName = roadAddress[0] + " " + roadAddress[1];
        } catch (Exception e) {
            roadAddressName = meeting.getRoadAddressName();
        }
        try {
            url = images.get(0).getImageUrl();
        } catch (Exception e) {
            url = null;
        }

        return GetLatestMeetingListRes.builder()
                .meetingId(meeting.getId())
                .categoryId(meeting.getCategoryId())
                .username(meeting.getUsername())
                .title(meeting.getTitle())
                .imageUrl(url)
                .roadAddressName(roadAddressName)
                .placeName(meeting.getPlaceName())
                .detailedAddress(meeting.getDetailedAddress())
                .participantsCount(meeting.getParticipantsCount())
                .maxParticipantsCount(meeting.getMaxParticipantsCount())
                .latitude(meeting.getLocationPoint().getY())
                .longitude(meeting.getLocationPoint().getX())
                .appointmentTime(meeting.getAppointmentTime())
                .endTime(meeting.getEndTime())
                .build();
    }
}
