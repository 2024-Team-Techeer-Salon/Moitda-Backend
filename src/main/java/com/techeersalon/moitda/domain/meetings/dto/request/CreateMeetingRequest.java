package com.techeersalon.moitda.domain.meetings.dto.request;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRequest {

    @NotBlank(message = "user id cannot be blank")
    private Long user_id;

    @NotBlank(message = "category_id cannot be blank")
    private Long category_id;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotBlank(message = "content cannot be blank")
    private String content;

    @NotBlank (message = "latitude cannot be blank")
    private Float latitude;

    @NotBlank(message = "iongitude cannot be blank")
    private Float iongitude;

    @NotBlank(message = "location cannot be blank")
    private String location;

    @NotBlank(message = "max_approval_required cannot be blank")
    private Boolean approval_required;

    @NotBlank(message = "appointment time cannot be blank")
    private String appointment_time;

    public Meeting toEntity(Meeting meeting){
        return Meeting.builder()
                .user_id(user_id)
                .category_id(meeting.getCategory_id())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .latitude(meeting.getLatitude())
                .longitude(meeting.getLongitude())
                .location(meeting.getLocation())
                .maxParticipantsCount(getMax_approval_required())
                .approvalRequired(meeting.getApprovalRequired())
                .appointmentTime(meeting.)
                .build();
    }
}
