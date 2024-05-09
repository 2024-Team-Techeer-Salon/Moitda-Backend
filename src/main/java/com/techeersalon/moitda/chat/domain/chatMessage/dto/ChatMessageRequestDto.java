package com.techeersalon.moitda.chat.domain.chatMessage.dto;

import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private Long userId;
    private String message;
    private Long roomId;;
    private ChatMessage.MessageType messageType;

    @Builder
    public ChatMessageRequestDto(Long userId, String message, Long roomId, ChatMessage.MessageType messageType) {
        this.userId = userId;
        this.message = message;
        this.roomId = roomId;
        this. messageType =  messageType;
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .userid(this.userId)
                .message(this.message)
                .roomId(this.roomId)
                .messageType(messageType)
                .build();
    }


}