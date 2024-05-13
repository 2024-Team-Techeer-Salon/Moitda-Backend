package com.techeersalon.moitda.domain.meetings.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangeMeetingInfoReq {
    @NotNull(message = "category_id cannot be blank")
    private Long categoryId;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotBlank(message = "content cannot be blank")
    private String content;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotBlank(message = "address_detail cannot be blank")
    private String addressDetail;

    @NotBlank(message = "builing_name cannot be blank")
    private String buildingName;

    @NotNull(message = "max_participants_count cannot be blank")
    private Integer maxParticipantsCount;

    @NotBlank(message = "appointment time cannot be blank")
    private String appointmentTime;

    @NotBlank(message = "image")
    private String image;

}
