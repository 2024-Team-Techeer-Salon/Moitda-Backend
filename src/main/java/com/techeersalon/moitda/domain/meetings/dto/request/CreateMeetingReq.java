package com.techeersalon.moitda.domain.meetings.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.user.entity.User;
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
public class CreateMeetingReq {

    @NotNull(message = "category_id cannot be blank")
    private Long categoryId;

    @NotBlank(message = "title cannot be blank")
    private String title;

    private String content;

    @NotBlank(message = "place_name cannot be blank")
    private String placeName;

    @NotBlank(message = "road_address_name cannot be blank")
    private String roadAddressName;

    //@NotBlank(message = "detailed_address cannot be blank")
    private String detailedAddress;

    @NotNull(message = "max_participants_count cannot be blank")
    private Integer maxParticipantsCount;

    @NotNull(message = "max_approval_required cannot be blank")
    private Boolean approvalRequired;

    @NotBlank(message = "appointment time cannot be blank")
    private String appointmentTime;

    public Meeting toEntity(User user) {
        return Meeting.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .categoryId(categoryId)
                .title(title)
                .content(content)
                .address(roadAddressName)
                .buildingName(placeName)
                .addressDetail(detailedAddress)
                .maxParticipantsCount(maxParticipantsCount)
                .approvalRequired(approvalRequired)
                .appointmentTime(appointmentTime)
                .participantsCount(1)
                .build();
    }

    // 초기 데이터 생성용 생성자
}
