package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.domain.*;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import com.techeersalon.moitda.chat.mapper.ChatMapper;
import com.techeersalon.moitda.chat.repository.*;
import com.techeersalon.moitda.chat.domain.UserChatRoom;
import com.techeersalon.moitda.chat.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;


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

        ChatMessage message = chatMapper.toChatMessage(sender, chatRoomEntity, messageRequestDto);
        this.chatMessageRepository.save(message);
    }


    /** ChatMessage 삭제 */
    @Transactional
    public void delete(final Long chatMessageId) {
        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatMessage가 존재하지 않습니다. chatMessageId = " + chatMessageId));
        this.chatMessageRepository.delete(chatMessageEntity);
    }

    @Transactional
    public List<ChatMessageResponseDto> findChatMessage(ChatRoom chatRoom) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom(chatRoom);

        return chatMapper.toChatMessageList(chatMessages);
    }
}