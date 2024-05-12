package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.mapper.ChatMapper;
import com.techeersalon.moitda.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.chat.domain.UserChatRoom;
import com.techeersalon.moitda.chat.dto.ChatRoomRequestDto;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import com.techeersalon.moitda.chat.repository.UserChatRoomRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;


    /** ChatRoom 생성 */
    @Transactional
    public ChatRoom createChatRoom(String roomName) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .name(roomName) // 이름 설정
                .build();
        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }

    @Transactional
    public void createUserChatRoom(User user, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + roomId));
        UserChatRoom newUserChatRoom = chatMapper.toUserChatRoom(user, chatRoom);

        userChatRoomRepository.save(newUserChatRoom);
    }

    /*채팅방에 유저 추가*/
    @Transactional
    public void addUserToRoom(Long roomId, Long userId) {
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setChatRoom(chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found")));
        userChatRoom.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Member not found")));
        userChatRoomRepository.save(userChatRoom);
    }

    @Transactional
    public List<ChatRoomResponseDto> findUserChatRoomByUserId(Long userId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByUserId(userId);
        List<ChatRoomResponseDto> chatRoomResList = new ArrayList<>();

        for (UserChatRoom userChatRoom : userChatRooms) {
            ChatRoom chatRoom = userChatRoom.getChatRoom();
            // 모든 멤버 조회
            List<User> allMembers = userChatRoomRepository.findByChatRoom(chatRoom)
                    .stream()
                    .map(UserChatRoom::getUser)
                    .collect(Collectors.toList());
            // 현재 사용자를 제외한 멤버 필터링
            List<User> otherMembers = allMembers.stream()
                    .filter(m -> !m.getId().equals(userId))
                    .collect(Collectors.toList());

            ChatRoomResponseDto chatRoomRes = chatMapper.toChatRoomDto(chatRoom, otherMembers);
            chatRoomResList.add(chatRoomRes);
        }
        return chatRoomResList;
    }

    public Optional<ChatRoom> findById(Long roomId) {
        // 데이터베이스에서 채팅방 조회
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);

        // Optional로 반환
        return chatRoomOptional;
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
