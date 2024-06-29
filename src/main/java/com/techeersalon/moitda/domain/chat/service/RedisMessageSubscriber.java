package com.techeersalon.moitda.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static com.techeersalon.moitda.domain.chat.service.RedisListSubscriber.extractNumber;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisMessageSubscriber implements MessageListener{

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("subscribe input");
            String patternchannel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageRes chatMessageRes = objectMapper.readValue(publishMessage, ChatMessageRes.class);
            String channel = extractNumber(patternchannel);
            log.info("Sending message to STOMP channel: /sub/chat/room/{}", channel);
            messageSendingOperations.convertAndSend("/sub/chat/room/" + channel, chatMessageRes); /*메세지 보내기*/
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }

//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        if (message.get)
//
//
//        String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
//        String channel = String.valueOf(message.getChannel());
//        ChatMessageRes chatMessageRes = objectMapper.readValue(publishMessage, ChatMessageRes.class);
//
//        log.info("Sending message to STOMP channel: /sub/chat/room/{}", chatMessageRes.getRoomId());
//        messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessageRes.getRoomId(), chatMessageRes);
//
//    }


}
