package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoRequest;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE meeting SET is_deleted = true WHERE meeting_id = ?")
@Where(clause = "is_deleted = false")
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "participants_count", nullable = false)
    private Integer participantsCount;

    @Column(name = "max_participants_count", nullable = false)
    private Integer maxParticipantsCount;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "address_detail")
    private String addressDetail;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;

    @Column(name = "end_time")
    private String endTime;

    public void increaseParticipantsCnt() {
        this.participantsCount++;
    }

    public void updateInfo(ChangeMeetingInfoRequest dto){
        this.categoryId = dto.getCategoryId();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.buildingName = dto.getBuildingName();
        this.address = dto.getBuildingName();
        this.addressDetail = dto.getAddressDetail();
        this.maxParticipantsCount = dto.getMaxParticipantsCount();
        this.appointmentTime = dto.getAppointmentTime();
        this.image = dto.getImage();
    }

    public void updateEndTime(String endTime) {
        this.endTime = endTime;
    }
}
