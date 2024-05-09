package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoom;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoomRequestDto;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoomResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /** ChatRoom 조회 */
    @Transactional
    public ChatRoomResponseDto findById(final Long id) {
        ChatRoom entity = this.chatRoomRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. id = " + id));
        return new ChatRoomResponseDto(entity);
    }

    /** ChatRoom 생성 */
    @Transactional
    public Long save(final ChatRoomRequestDto requestDto) {
        return this.chatRoomRepository.save(requestDto.toEntity()).getId();
    }

    /** ChatRoom 삭제 */
    public void delete(final Long id) {
        ChatRoom entity = this.chatRoomRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. id = " + id));
        this.chatRoomRepository.delete(entity);
    }

    /**ChatRoom 검색목록조회 - 최신순, List**/
    @Transactional
    public List<ChatRoomResponseDto> findAllByRoomNameDesc(String roomName) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ChatRoom> chatRoomList = this.chatRoomRepository.findAllByRoomNameContaining(roomName, sort);
        return chatRoomList.stream().map(ChatRoomResponseDto::new).collect(Collectors.toList());
    }

    /** ChatRoom 목록조회 - 최신순, List */
    @Transactional
    public List<ChatRoomResponseDto> findALlDesc(Long userid) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ChatRoom> chatRoomList = this.chatRoomRepository.findAll(sort);
        return chatRoomList.stream().map(ChatRoomResponseDto::new).collect(Collectors.toList());
    }

    public void updateLastReadChat(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 속해있지 않은 회원입니다."));
        ChatMessage chatMessage = chatMessageRepository
                .findById(userId).orElseThrow(() -> new IllegalArgumentException("채팅 내역이 존재하지 않습니다."));
        chatRoom.updateChatId(chatMessage.getId());
        chatRoom.updateRecentChat(chatMessage.getMessage());
    }
}
