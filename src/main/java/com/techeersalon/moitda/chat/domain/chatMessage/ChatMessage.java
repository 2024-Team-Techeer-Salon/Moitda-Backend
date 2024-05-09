package com.techeersalon.moitda.chat.domain.chatMessage;

import com.techeersalon.moitda.chat.domain.BaseTime;
import com.techeersalon.moitda.chat.domain.chatMessage.dto.ChatMessageDto;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoom;
import com.techeersalon.moitda.domain.user.entity.User;
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

    @Column(name = "user_id", nullable = false)
    private Long userid;


    @Column(columnDefinition = "TEXT")
    private String message; // comment

    @Column(name = "room_id", nullable = false)
    private Long roomId;


    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType messageType;

    @Builder
    public ChatMessage(Long userid, String message, Long roomId, MessageType messageType) {
        this.userid = userid;
        this.message = message;
        this.roomId = roomId;
        this. messageType =  messageType;
        this.createdDate = LocalDateTime.now();
    }
    public static ChatMessage createChat(Long roomId,Long userid, String message, MessageType messageType) {
        return ChatMessage.builder()
                .roomId(roomId)
                .userid(userid)
                .message(message)
                .messageType(messageType)
                .build();
    }

}