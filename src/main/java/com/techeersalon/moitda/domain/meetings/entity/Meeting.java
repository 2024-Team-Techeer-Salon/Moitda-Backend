package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.techeersalon.moitda.domain.meetings.entity.Category;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

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
    @Column(name = "meeting_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "category_id", nullable = false)
    private Long category_id;

    @Column(name = "title",nullable = true)
    private String title;

    @Column(name = "participants_count", nullable = false)
    private Integer participantsCount;

    @Column(name = "max_participants_count", nullable = false)
    private Integer maxParticipantsCount;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Column(name = "location", length = 20, nullable = false)
    private String location;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image", length = 256, nullable = false)
    private String image;

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "endtime")
    private LocalDateTime endTime;
}
