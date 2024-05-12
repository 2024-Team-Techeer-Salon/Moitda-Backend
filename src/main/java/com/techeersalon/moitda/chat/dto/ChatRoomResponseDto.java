package com.techeersalon.moitda.chat.dto;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.domain.UserChatRoom;
import com.techeersalon.moitda.domain.user.entity.User;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder

public class ChatRoomResponseDto {
    private Long id;
    private String roomName;
    private List<MemberDetail> members;


    @Getter
    @Setter
    @Builder
    public static class MemberDetail {
        private Long id;
        private String name;
        private String bannerImage;

    }


}
