package com.techeersalon.moitda.chat.domain.chatRoom;

import com.techeersalon.moitda.chat.domain.BaseTime;
import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@NoArgsConstructor
@Getter
@Entity
public class ChatRoom extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;

    @Builder
    public ChatRoom(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    @Column
    private String recentChat;

    public void updateRecentChat(String recentChat) {
        this.recentChat = recentChat;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }
}