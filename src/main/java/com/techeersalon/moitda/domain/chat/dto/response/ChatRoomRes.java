package com.techeersalon.moitda.domain.chat.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomRes {
    private Long id;
    private Long lastMessage;
    private List<MemberDetail> members; // 채팅방 멤버 정보

    @Getter
    @Setter
    @Builder
    public static class MemberDetail {
        private Long id;
        private String name;
    }

}
