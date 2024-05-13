package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
    private Long id;

//    @Builder
//    public ChatRoomRequestDto(Long id) {
//        this.id = id;
//    }
}
