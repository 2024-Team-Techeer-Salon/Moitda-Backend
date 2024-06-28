package com.techeersalon.moitda.domain.chat.entity;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_message SET is_deleted = true WHERE chat_message_id = ?")
@Where(clause = "is_deleted = false")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(name = "user_Id")
    private Long userid;


    @Column(columnDefinition = "TEXT")
    private String message; // comment

    @Column(name = "meeting_Id")
    private Long meetingId;


    public enum MessageType {
        ENTER, TALK
    }

    @Column(nullable = false)
    private MessageType messageType;

}