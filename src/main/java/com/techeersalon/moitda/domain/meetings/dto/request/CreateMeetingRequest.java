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
    private Long categoryId;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotBlank(message = "content cannot be blank")
    private String content;

    @NotBlank(message = "builing_name cannot be blank")
    private String buildingName;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotBlank(message = "max_participants_count cannot be blank")
    private Integer maxParticipantsCount;

    @NotBlank(message = "max_approval_required cannot be blank")
    private Boolean approvalRequired;

    @NotBlank(message = "appointment time cannot be blank")
    private String appointmentTime;

    public Meeting toEntity(User user){
        return Meeting.builder()
                .userId(user.getId())
                .categoryId(categoryId)
                .title(title)
                .content(content)
                .address(address)
                .buildingName(buildingName)
                .maxParticipantsCount(maxParticipantsCount)
                .approvalRequired(approvalRequired)
                .appointmentTime(LocalDateTime.parse(appointmentTime))
                .build();
    }
}