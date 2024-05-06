package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
    private String roomName;

    @Builder
    public ChatRoomRequestDto(String roomName) {
        this.roomName = roomName;
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .roomName(this.roomName)
                .build();
    }
}
