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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

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

    @Column(name = "road_address_name", nullable = false)
    private String roadAddressName;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "detailed_address")
    private String detailedAddress;

    @Column(name = "location_point")
    //@Type(org.hibernate.spatial.)
    private Point locationPoint;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
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
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(dto.getLongitude(), dto.getLatitude());
        Point point = geometryFactory.createPoint(coord);

        this.categoryId = dto.getCategoryId();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.placeName = dto.getPlaceName();
        this.roadAddressName = dto.getRoadAddressName();
        this.detailedAddress = dto.getDetailedAddress();
        this.locationPoint = point;
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
