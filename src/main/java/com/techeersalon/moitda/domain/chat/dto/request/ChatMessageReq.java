package com.techeersalon.moitda.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageReq {
    //private String sender;
    private String message;
    //private Long roomId;;

//    @Builder
//    public ChatMessageRequestDto(Long userId, String message, Long roomId, ChatMessage.MessageType messageType) {
//        this.userId = userId;
//        this.message = message;
//        this.roomId = roomId;
//        this. messageType =  messageType;
//    }


}