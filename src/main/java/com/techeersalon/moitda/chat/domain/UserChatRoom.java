package com.techeersalon.moitda.chat.domain;

import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_chat_room SET is_deleted = true WHERE user_chat_room_id = ?")
@Where(clause = "is_deleted = false")
public class UserChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

//    @Column
//    private String recentChat;
//
//    public void updateRecentChat(String recentChat) {
//        this.recentChat = recentChat;
//    }
//
//    @Column
//    private Long lastuserId;
//
//    public void updateLastUserId(Long lastuserId) {
//        this.lastuserId = lastuserId;
//    }
}