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
    @Column(name = "meetingParticipantId")
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

    public void notNeedToApprove() {
        this.isWaiting = false;
    }
}
