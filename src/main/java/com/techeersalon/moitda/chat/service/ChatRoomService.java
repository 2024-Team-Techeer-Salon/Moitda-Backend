package com.techeersalon.moitda.chat.service;

import com.techeersalon.moitda.chat.dao.ChatRoomRepository;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.dto.ChatRoomRequestDto;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

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
    public List<ChatRoomResponseDto> findALlDesc() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ChatRoom> chatRoomList = this.chatRoomRepository.findAll(sort);
        return chatRoomList.stream().map(ChatRoomResponseDto::new).collect(Collectors.toList());
    }
}
