package com.techeersalon.moitda.domain.chat.dto.request;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
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
//    private ChatMessage.MessageType type;  // 메시지 타입 필드 추가

//    @Builder
//    public ChatMessageRequestDto(Long userId, String message, Long roomId, ChatMessage.MessageType messageType) {
//        this.userId = userId;
//        this.message = message;
//        this.roomId = roomId;
//        this. messageType =  messageType;
//    }


}