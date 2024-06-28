package com.techeersalon.moitda.domain.chat.entity;

import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET is_deleted = true WHERE chatroom_id = ?")
@Where(clause = "is_deleted = false")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "meeting_Id", nullable = false)
    private Long meetingId;

    @ManyToMany
    @JoinTable(
            name = "chatroom_user",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final List<User> members = new ArrayList<>();

    @Column(name = "last_Message_Id")
    private Long lastMessageId;

    // Lombok의 @Builder를 사용할 때 필드 추가를 반영하기 위한 빌더 패턴 설정
    @Builder
    public ChatRoom(Long id, Long meetingId, List<ChatMessage> chatMessageList, List<User> members) {
        this.id = id;
        this.meetingId = meetingId;
        if (members != null) {
            this.members.addAll(members);
        }
    }

    public void addMember(User user) {
        this.members.add(user);
        user.getChatRooms().add(this);
    }

    public void removeMember(User user) {
        this.members.remove(user);
        user.getChatRooms().remove(this);
    }



}