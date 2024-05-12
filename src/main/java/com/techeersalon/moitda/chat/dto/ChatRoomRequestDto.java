package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.UserChatRoom;
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
}