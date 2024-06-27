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
    private final RedisMessageListenerContainer redisMessageListener;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("subscribe input");

            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            String channel = new String(message.getChannel());
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            ChatMessageRes chatMessageRes = chatMapper.toChatMessageDto(chatMessage);

            // 채널 이름에서 roomId를 추출합니다. 가정: channel이 "chatroom:1" 형식이라면
//            String[] parts = channel.split(":");
//            String roomId = parts.length > 1 ? parts[1] : channel;

            log.info("Sending message to STOMP channel: /sub/chat/room/{}", channel);
            messageSendingOperations.convertAndSend("/sub/chat/room/" + channel, chatMessageRes); /*메세지 보내기*/
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }

//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            // Redis에서 수신한 메시지를 디시리얼라이즈
//            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//
//            // JSON 문자열을 ChatMessageReq 객체로 변환
//            ChatMessageReq messageReq = objectMapper.readValue(publishMessage, ChatMessageReq.class);
//
//            // roomId를 패턴에서 추출
//            String roomId = new String(pattern).split(":")[1];
//
//            User loginUser = userService.getLoginUser();
//
//            // 채팅 메시지를 처리
//            ChatMessageRes responseDto = chatMessageService.createChatMessage(loginUser, Long.valueOf(roomId), messageReq);
//
//            // WebSocket을 통해 클라이언트로 메시지 전송
//            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, responseDto);
//            log.info("Message published successfully");
//
//        } catch (Exception e) {
//            log.error("Failed to process message", e);
//            throw new MessageNotFoundException();
//        }
//    }
}
