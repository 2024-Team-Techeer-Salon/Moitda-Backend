package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String senderId;
    private String message;
    private ChatRoom chatRoom;

    @Builder
    public ChatMessageRequestDto(String senderId, String message, ChatRoom chatRoom) {
        this.senderId = senderId;
        this.message = message;
        this.chatRoom = chatRoom;
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .senderId(this.senderId)
                .message(this.message)
                .chatRoom(this.chatRoom)
                .build();
    }

}