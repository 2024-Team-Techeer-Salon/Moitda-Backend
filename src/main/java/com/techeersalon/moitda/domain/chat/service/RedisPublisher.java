package com.techeersalon.moitda.domain.chat.service;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(ChatMessage message) {
        String channel = "chatroom-" + message.getMeetingId();
        redisTemplate.convertAndSend(channel, message);
    }
}
