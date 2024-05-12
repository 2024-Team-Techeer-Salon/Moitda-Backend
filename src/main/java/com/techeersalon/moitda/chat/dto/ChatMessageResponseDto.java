package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
@Builder
public class ChatMessageResponseDto {
    private String sender;
    private String content;
    private LocalDateTime sendDate;

}