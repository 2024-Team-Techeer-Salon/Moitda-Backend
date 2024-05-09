package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.repository.*;
import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoom;
import com.techeersalon.moitda.chat.domain.chatMessage.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.domain.chatMessage.dto.ChatMessageResponseDto;
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

    /** ChatMessage 조회 */
    @Transactional
    public ChatMessageResponseDto findById(final Long chatMessageId) {
        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatMessage가 존재하지 않습니다. chatMessageId = " + chatMessageId));
        return new ChatMessageResponseDto(chatMessageEntity);
    }

    /** ChatMessage 생성 */
    @Transactional
    public Long save(final Long chatRoomId, final ChatMessageRequestDto requestDto) {
        ChatRoom chatRoomEntity = this.chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. chatRoomId = " + chatRoomId));
        requestDto.setRoomId(chatRoomEntity.getId());
        return this.chatMessageRepository.save(requestDto.toEntity()).getId();
    }


    /** ChatMessage 삭제 */
    @Transactional
    public void delete(final Long chatMessageId) {
        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatMessage가 존재하지 않습니다. chatMessageId = " + chatMessageId));
        this.chatMessageRepository.delete(chatMessageEntity);
    }

    @Transactional
    public List<ChatMessageResponseDto> findAllByChatRoomIdDesc(final Long chatRoomId) {
        ChatRoom chatRoomEntity = this.chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. chatRoomId = " + chatRoomId));
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ChatMessage> chatMessageList = this.chatMessageRepository.findAllByChatRoom(chatRoomEntity, sort);
        return chatMessageList.stream().map(ChatMessageResponseDto::new).collect(Collectors.toList());
    }
}