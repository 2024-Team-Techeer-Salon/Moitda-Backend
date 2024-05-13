package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.mapper.ChatMapper;
import com.techeersalon.moitda.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;


    /** ChatRoom 생성 */
    @Transactional
    public ChatRoom createChatRoom(Long meetingId) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .meetingId(meetingId) // 이름 설정
                .build();
        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }


    /*채팅방에 유저 추가*/
    @Transactional
    public void addUserToRoom(Long roomId, Long userId) {
    }

    @Transactional
    public Optional<ChatRoom> findById(Long roomId) {
        // 데이터베이스에서 채팅방 조회
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);
        // Optional로 반환
        return chatRoomOptional;
    }

    @Transactional
    public List<ChatRoomResponseDto> getChatRoomsByUser(User user) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMembers(user);
        List<ChatRoomResponseDto> chatRoomDtoList = ChatMapper.toChatRoomDtoList(chatRooms);
        return chatRoomDtoList;
    }

    @Transactional
    public void deleteRoomAndMessages(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        chatRoomRepository.delete(chatRoom);
        //chatRoom.delete();
        List<ChatMessage> chatMessageList = chatMessageRepository.findByMeetingId(roomId);
        //chatMessage.delete();
        chatMessageRepository.deleteAll(chatMessageList);
    }

//    public boolean duplicatedUserChatRoom(Member member) {
//        return userChatRoomRepository.existsByMemberId(member.getId());
//    }



//    public void updateLastReadChat(Long roomId, Long userId) {
//        UserChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("채팅방에 속해있지 않은 회원입니다."));
//        ChatMessage chatMessage = chatMessageRepository
//                .findById(userId).orElseThrow(() -> new IllegalArgumentException("채팅 내역이 존재하지 않습니다."));
//        chatRoom.updateLastUserId(chatMessage.getUser().getId());
//        chatRoom.updateRecentChat(chatMessage.getMessage());
//    }
}
