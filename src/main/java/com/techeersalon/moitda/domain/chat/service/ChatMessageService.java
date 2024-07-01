package com.techeersalon.moitda.domain.chat.service;

import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.request.ChatMessageReq;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.exception.ChatRoomNotFoundException;
import com.techeersalon.moitda.domain.chat.exception.MessageNotFoundException;
import com.techeersalon.moitda.domain.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.domain.chat.repository.ChatRoomRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatMessageService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;




    /**
     * ChatMessage 생성
     *
     * @return
     */
    @Transactional
    public ChatMessageRes createChatMessage(User sender, Long roomId, ChatMessageReq messageRequestDto) {
        this.chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFoundException::new);
        ChatMessage entity = chatMapper.toChatMessage(sender, roomId, messageRequestDto);
        ChatMessage chatMessage = chatMessageRepository.save(entity);
        this.updateLastChatMessage(roomId, chatMessage.getId());
        return chatMapper.toChatMessageDto(chatMessage);
    }


    /** ChatMessage 삭제 */
    @Transactional
    public void delete(final Long chatMessageId) {
        ChatMessage chatMessageEntity = this.chatMessageRepository.findById(chatMessageId).orElseThrow(
                MessageNotFoundException::new);
        this.chatMessageRepository.delete(chatMessageEntity);
    }



    /*채팅방의 메시지 조회*/
    @Transactional
    public List<ChatMessageRes> findChatMessageList(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByMeetingId(roomId);
        return chatMapper.toChatMessageDtoList(chatMessages);
    }

    /*채팅방 메시지 조회 무한 스크롤*/
    @Transactional
    public List<ChatMessageRes> getLatestMessageList(Long meetingId, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createAt")));
        Page<ChatMessage> chatMessages = chatMessageRepository.findPagesByMeetingId(meetingId, pageable);

        return chatMapper.PageToChatMessageDto(chatMessages);
        // return meetings.map(GetLatestMeetingListResponse::of);
    }

    public void updateLastChatMessage(Long roomId, Long messageId){
        ChatRoom chatRoom = this.chatRoomRepository.findById(roomId).orElseThrow(
                ChatRoomNotFoundException::new);
        chatRoom.updateLastMessageId(messageId);
        this.chatRoomRepository.save(chatRoom);

    }

    public void updateLastReadChat(String channel, LocalDateTime disconnectTime) {

    }
}