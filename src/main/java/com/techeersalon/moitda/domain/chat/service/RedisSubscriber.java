package com.techeersalon.moitda.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.request.ChatMessageReq;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.exception.MessageNotFoundException;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener{

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMapper chatMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("subscribe input");

            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            String channel = String.valueOf(message.getChannel());
            ChatMessageRes chatMessageRes = objectMapper.readValue(publishMessage, ChatMessageRes.class);


            // 채널 이름에서 roomId를 추출합니다. 가정: channel이 "chatroom:1" 형식이라면
//            String[] parts = channel.split(":");
//            String roomId = parts.length > 1 ? parts[1] : channel;

            log.info("Sending message to STOMP channel: /sub/chat/room/{}", chatMessageRes.getRoomId());
            messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessageRes.getRoomId(), chatMessageRes); /*메세지 보내기*/
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }


}
