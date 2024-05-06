package com.techeersalon.moitda.chat.domain;

import com.techeersalon.moitda.chat.BaseTime;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Builder
public class ChatRoom extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;

    /*private long userCount; // 채팅방 인원수

    private HashMap<String, String> userlist = new HashMap<String, String>();*/

    /* delete on cascade 활용 같은데 우리는 delete_atd을 바꾸는 식이라 쓰면 안될듯 */
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
//    private List<ChatMessage> chatMessageList;
    @Builder
    public ChatRoom(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public static ChatRoom createRoom(String roomName) {
        return ChatRoom.builder()
                .roomName(roomName)
                .build();
    }

//    public Long update(ChatRoomRequestDto requestDto) {
//        this.roomName = requestDto.getRoomName();
//        return this.id;
//    }
}