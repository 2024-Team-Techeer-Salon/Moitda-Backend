package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MaxParticipantsNotExceededException;
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

    @Column(name = "username", nullable = false)
    private String username;

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

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;

    @Column(name = "end_time")
    private String endTime;

    public void increaseParticipantsCnt() {
        this.participantsCount++;
    }

    public void updateInfo(ChangeMeetingInfoReq dto){
        this.categoryId = dto.getCategoryId();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.buildingName = dto.getPlaceName();
        this.address = dto.getRoadAddressName();
        this.addressDetail = dto.getDetailedAddress();
        this.maxParticipantsCount = dto.getMaxParticipantsCount();
        this.appointmentTime = dto.getAppointmentTime();
    }

    public void updateEndTime(String endTime) {
        this.endTime = endTime;
    }

    // 최대 참가 인원 유효성 검사 메서드
    public void validateMaxParticipantsCount() {
        if (maxParticipantsCount < 2 || maxParticipantsCount > 100) { // 모집 인원이 2명보다 적거나 100보다 클 경우
            throw new MaxParticipantsNotExceededException();
        }
    }
}
