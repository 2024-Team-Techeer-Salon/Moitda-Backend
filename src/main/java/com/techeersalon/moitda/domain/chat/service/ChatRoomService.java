package com.techeersalon.moitda.domain.chat.service;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.exception.ChatRoomNotFoundException;
import com.techeersalon.moitda.domain.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.domain.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UserNotFoundException;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.domain.user.service.UserService;
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
    private final UserService userService;
    private final UserRepository userRepository;


    /**
     * ChatRoom 생성
     */
    @Transactional
    public ChatRoom createChatRoom(Long meetingId) {
        User user = userService.getLoginUser();
        ChatRoom newChatRoom = ChatRoom.builder()
                .meetingId(meetingId) // 이름 설정
                .build();
        newChatRoom.addMember(user);
        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }


    /*채팅방에 유저 추가*/
    @Transactional
    public ChatRoomRes addUserToRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(UserNotFoundException::new);

        chatRoom.addMember(user);

        chatRoomRepository.save(chatRoom);
        return chatMapper.toChatRoomDto(chatRoom);
    }

    @Transactional
    public Optional<ChatRoom> findById(Long roomId) {
        // 데이터베이스에서 채팅방 조회
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);
        // Optional로 반환
        return chatRoomOptional;
    }

    @Transactional
    public List<ChatRoomRes> getChatRoomsByUser() {
        User user = userService.getLoginUser();
        List<ChatRoom> chatRooms = chatRoomRepository.findByMembers(user);
        return chatMapper.toChatRoomDtoList(chatRooms);
    }

    @Transactional
    public void deleteRoomAndMessages(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        System.out.println(chatRoom.getId());
        chatRoomRepository.delete(chatRoom);
        System.out.println(chatRoom.getId());

        //chatRoom.delete();
        List<ChatMessage> chatMessageList = chatMessageRepository.findByMeetingId(roomId);
        System.out.println(chatMessageList);
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
