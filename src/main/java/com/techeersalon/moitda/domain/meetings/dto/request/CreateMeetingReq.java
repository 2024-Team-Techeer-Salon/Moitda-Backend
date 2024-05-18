package com.techeersalon.moitda.domain.meetings.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @NotBlank(message = "builing_name cannot be blank")
    private String buildingName;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotBlank(message = "address_detail cannot be blank")
    private String addressDetail;

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
                .address(address)
                .buildingName(buildingName)
                .addressDetail(addressDetail)
                .maxParticipantsCount(maxParticipantsCount)
                .approvalRequired(approvalRequired)
                .appointmentTime(appointmentTime)
                .participantsCount(1)
                .build();
    }

    // 초기 데이터 생성용 생성자
    public CreateMeetingReq(Long categoryId, String title,
                            String buildingName, String address, String addressDetail,
                            Integer maxParticipantsCount, Boolean approvalRequired, String appointmentTime){
        this.categoryId = categoryId;

        this.title = title;

        this.buildingName = buildingName;

        this.address = address;

        this.addressDetail = addressDetail;

        this.maxParticipantsCount = maxParticipantsCount;

        this.approvalRequired = approvalRequired;

        this.appointmentTime = appointmentTime;

    }
}
