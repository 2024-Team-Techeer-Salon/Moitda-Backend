package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.mapper.ChatMapper;
import com.techeersalon.moitda.chat.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final int pageSize = 32;


//    /** ChatMessage 조회 */
//    @Transactional
//    public ChatMessageResponseDto findById(final Long chatMessageId) {
//        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
//                () -> new IllegalArgumentException("해당 ChatMessage가 존재하지 않습니다. chatMessageId = " + chatMessageId));
//        return new ChatMessageResponseDto(chatMessageEntity);
//    }
    /**
     * ChatMessage 생성
     */
    @Transactional
    public void save(User sender, Long roomId, ChatMessageRequestDto messageRequestDto) {
        ChatRoom chatRoomEntity = this.chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. chatRoomId = " + roomId));
        //messageRequestDto.setRoomId(chatRoomEntity.getId());

        ChatMessage message = chatMapper.toChatMessage(sender, roomId, messageRequestDto);
        this.chatMessageRepository.save(message);
    }


    /** ChatMessage 삭제 */
    @Transactional
    public void delete(final Long chatMessageId) {
        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatMessage가 존재하지 않습니다. chatMessageId = " + chatMessageId));
        this.chatMessageRepository.delete(chatMessageEntity);
    }

    /*채팅방의 메시지 조회*/
    @Transactional
    public List<ChatMessageResponseDto> findChatMessage(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByMeetingId(roomId);
        return chatMapper.toChatMessageDtoList(chatMessages);
    }

    /*채팅방 메시지 조회 무한 스크롤*/
//    @Transactional
//    public Page<GetLatestMessageListResponseDto> findLatestMessageList(int page){
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createAt"));
//        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
//        Page<ChatMessage> chatMessages = chatMessageRepository.findAll(pageable);
//
//        return chatMessages.map(ChatMapper.of(chatMessages));
//        // return meetings.map(GetLatestMeetingListResponse::of);
//    }

}