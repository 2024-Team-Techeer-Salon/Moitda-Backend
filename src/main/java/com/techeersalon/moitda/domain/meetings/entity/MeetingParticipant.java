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
@SQLDelete(sql = "UPDATE meeting_participant SET is_deleted = true WHERE meeting_participant_id = ?")
@Where(clause = "is_deleted = false")
public class MeetingParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_participant_id")
    private Long id;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "is_waiting", nullable = false)
    private Boolean isWaiting;

    public void notNeedToApprove() {
        this.isWaiting = false;
    }
}
