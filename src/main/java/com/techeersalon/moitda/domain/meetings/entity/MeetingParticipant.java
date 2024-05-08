package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
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

    @Column(name = "is_host", nullable = false)
    private Boolean isHost;

    @Column(name = "is_waiting", nullable = false)
    private Boolean isWaiting;

    public MeetingParticipant(Long meetingId, Long userId){
        this.meetingId = meetingId;
        this.userId = userId;
        this.isHost = false;
        this.isWaiting =true;
    }
}
