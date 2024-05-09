package com.techeersalon.moitda.chat.domain.chatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomInviteDto {
    private Long userId;
    private String roomName;
    private String username;
}

