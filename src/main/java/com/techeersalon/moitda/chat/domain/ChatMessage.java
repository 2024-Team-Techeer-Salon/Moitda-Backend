package com.techeersalon.moitda.chat.domain;

import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_message SET is_deleted = true WHERE chat_message_id = ?")
@Where(clause = "is_deleted = false")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime sendDate;


    @Column(columnDefinition = "TEXT")
    private String message; // comment


    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


}