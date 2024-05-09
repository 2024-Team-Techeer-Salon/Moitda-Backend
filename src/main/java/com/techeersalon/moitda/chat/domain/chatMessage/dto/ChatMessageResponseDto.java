package com.techeersalon.moitda.chat.domain.chatMessage.dto;

import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatMessageResponseDto {
    private Long id;
    private Long userId;
    private String message;
    private Long roomId;
//    private ChatMessage.MessageType messageType;
    private String createdDate;
    private String deletedDate;

    public ChatMessageResponseDto(ChatMessage entity) {
        this.id = entity.getId();
        this.userId = entity.getUserid();
        this.roomId = entity.getRoomId();
        this.message = entity.getMessage();
//        this. messageType =  entity.getMessageType();
        this.createdDate = entity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.deletedDate = entity.getDeletedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}