package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatMessageResponseDto {
    private Long id;
    private String senderId;
    private String message;
    private String createdDate;
    private String deletedDate;

    public ChatMessageResponseDto(ChatMessage entity) {
        this.id = entity.getId();
        this.senderId = entity.getSenderId();
        this.message = entity.getMessage();
        this.createdDate = entity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.deletedDate = entity.getDeletedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}