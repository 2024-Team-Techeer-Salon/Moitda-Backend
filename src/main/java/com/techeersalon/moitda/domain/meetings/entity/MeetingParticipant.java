package com.techeersalon.moitda.domain.meetings.entity;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
@Where(clause = "is_deleted = false")
public class MeetingParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_participantId")
    private Long id;

    @Column(name = "meetingId", nullable = false)
    private Long meetingId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "isWaiting", nullable = false)
    private Boolean isWaiting;

    public MeetingParticipant(Long meetingId, Long userId){
        this.meetingId = meetingId;
        this.userId = userId;
        this.isWaiting = true;
    }
}
