package com.techeersalon.moitda.domain.chat.service;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.exception.AlreadyParticipatingException;
import com.techeersalon.moitda.domain.chat.exception.ChatRoomNotFoundException;
import com.techeersalon.moitda.domain.chat.exception.RoomOwnerCannotLeaveException;
import com.techeersalon.moitda.domain.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.domain.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
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
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

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

        // 이미 참가한 사용자인지 확인
        if (chatRoom.getMembers().contains(user)) {
            throw new AlreadyParticipatingException();
        }

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
        chatRoomRepository.delete(chatRoom);

        List<ChatMessage> chatMessageList = chatMessageRepository.findByMeetingId(roomId);

        chatMessageRepository.deleteAll(chatMessageList);
    }

    public void removeUserFromRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Meeting meeting = meetingRepository.findByUserId(chatRoom.getMeetingId());
        // 방장이 채팅방 나가는 경우 예외 처리.
        if (meeting.getUserId() == user.getId()) {
            throw new RoomOwnerCannotLeaveException();
        }

        chatRoom.removeMember(user);

    }

//    @Transactional
//    public void updateUserReadPosition(String roomId, String userId, String lastMessageId, boolean setRead) {
//        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//        hashOperations.put("userLastRead:" + roomId, userId, lastMessageId);
//
//        long lastMsgId = Long.parseLong(lastMessageId);
//        ChatMessage message = messageRepository.findById(lastMsgId).orElse(null);
//        if (message != null) {
//            message.setRead(true);
//            messageRepository.saveAndFlush(message); // 데이터베이스에 즉시 반영
//        }
//        // 캐시 갱신
//        redisTemplate.expire("userLastRead:" + roomId, 10, TimeUnit.MINUTES);
//    }
//
//    public String getUserLastReadPosition(String roomId, String userId) {
//        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//        return hashOperations.get("userLastRead:" + roomId, userId);
//    }

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
