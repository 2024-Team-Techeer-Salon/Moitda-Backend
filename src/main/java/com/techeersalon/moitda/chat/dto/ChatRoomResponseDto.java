package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatRoomResponseDto {
    private Long id;
    private String roomName;
    private String createdDate;
    private String deletedDate;

    public ChatRoomResponseDto(ChatRoom entity) {
        this.id = entity.getId();
        this.roomName = entity.getRoomName();
        this.createdDate = entity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.deletedDate = entity.getDeletedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
