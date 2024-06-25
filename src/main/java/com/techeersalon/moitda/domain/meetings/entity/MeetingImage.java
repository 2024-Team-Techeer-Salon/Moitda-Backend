package com.techeersalon.moitda.domain.meetings.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "meeting_image")
@NoArgsConstructor
public class MeetingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_image_id")
    private Long id;

    private String imageUrl;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    public MeetingImage(String imageUrl, Long meetingId) {
        this.imageUrl = imageUrl;
        this.meetingId = meetingId;
    }
}
