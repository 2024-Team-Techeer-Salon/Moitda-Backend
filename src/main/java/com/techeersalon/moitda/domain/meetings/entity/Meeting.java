package com.techeersalon.moitda.domain.meetings.entity;

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
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
@Where(clause = "is_deleted = false")
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetingId")
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "categoryId")
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

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "approvalRequired", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "appointmentTime")
    private String appointmentTime;

    @Column(name = "endTime")
    private String endTime;

    public void increaseParticipantsCnt() {
        this.participantsCount++;
    }
}
