package com.techeersalon.moitda.domain.meetings.dto.request;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRequest {

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

    @NotBlank(message = "max_participants_count cannot be blank")
    private Integer max_participants_count;

    @NotBlank(message = "max_approval_required cannot be blank")
    private Boolean approval_required;

    @NotBlank(message = "appointment time cannot be blank")
    private String appointment_time;

    public Meeting toEntity(User user){
        return Meeting.builder()
                .user_id(user.getId())
                .category_id(category_id)
                .title(title)
                .content(content)
                .latitude(latitude)
                .longitude(latitude)
                .location(location)
                .maxParticipantsCount(max_participants_count)
                .approvalRequired(approval_required)
                .appointmentTime(LocalDateTime.parse(appointment_time))
                .build();
    }
}