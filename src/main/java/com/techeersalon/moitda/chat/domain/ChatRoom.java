package com.techeersalon.moitda.chat.domain;

import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET is_deleted = true WHERE chat_room_id = ?")
@Where(clause = "is_deleted = false")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;  // 채팅방 이름 필드 추가

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private final List<ChatMessage> chatMessageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<UserChatRoom> userChatRoomList = new ArrayList<>();

    // Lombok의 @Builder를 사용할 때 필드 추가를 반영하기 위한 빌더 패턴 설정
    @Builder
    public ChatRoom(Long id, String name, List<ChatMessage> chatMessageList, List<UserChatRoom> userChatRoomList) {
        this.id = id;
        this.name = name;
        if (chatMessageList != null) {
            this.chatMessageList.addAll(chatMessageList);
        }
        if (userChatRoomList != null) {
            this.userChatRoomList.addAll(userChatRoomList);
        }
    }

}