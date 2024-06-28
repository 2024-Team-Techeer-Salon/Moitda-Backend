package com.techeersalon.moitda.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisListSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("subscribe input");
            User user = userService.getLoginUser();
            String publishList = redisTemplate.getStringSerializer().deserialize(message.getBody());
            List<ChatRoomRes> chatRoomRes = objectMapper.readValue(publishList, List.class);

            log.info("Sending message to STOMP channel: /sub/chat/room/{}",user.getId());
            messageSendingOperations.convertAndSend("/sub/chat/room/" + user.getId(), chatRoomRes); /*메세지 보내기*/
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
