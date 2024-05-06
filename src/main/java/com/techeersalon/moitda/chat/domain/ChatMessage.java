package com.techeersalon.moitda.chat.domain;

import com.techeersalon.moitda.chat.BaseTime;
import com.techeersalon.moitda.chat.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class ChatMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk
    private String senderId; //user_id

    @Column(columnDefinition = "TEXT")
    private String message; // comment

    @ManyToOne(fetch = FetchType.EAGER) //연관된 엔티티가 소유 엔티티와 함께 즉시 로드
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public enum MessageType{
        ENTER, TALK, LEAVE;

    }
    private ChatMessage.MessageType messageType;

    @Builder
    public ChatMessage(String senderId, String message, ChatRoom chatRoom) {
        this.senderId = senderId;
        this.message = message;
        this.chatRoom = chatRoom;
        this.createdDate = LocalDateTime.now();
    }
    public static ChatMessage createChat(ChatRoom chatroom, String senderId, String message) {
        return ChatMessage.builder()
                .chatRoom(chatroom)
                .senderId(senderId)
                .message(message)
                .build();
    }

}