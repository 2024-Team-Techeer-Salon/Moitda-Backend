package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Meeting SET is_deleted = true WHERE meeting_id = ?")
@Where(clause = "is_deleted = false")
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetingId")
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "categoryId", nullable = false)
    private Long categoryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "participantsCount", nullable = false)
    private Integer participantsCount;

    @Column(name = "maxParticipantsCount", nullable = false)
    private Integer maxParticipantsCount;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "buildingName", nullable = false)
    private String buildingName;

    @Column(name = "addressDetail", nullable = false)
    private String addressDetail;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image", length = 256, nullable = false)
    private String image;

    @Column(name = "approvalRequired", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "appointmentTime", nullable = false)
    private String appointmentTime;

    @Column(name = "endTime")
    private String endTime;

    public void increaseParticipantsCnt() {
        this.participantsCount++;
    }
}
