package com.techeersalon.moitda.domain.chat.service;

import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageRes message) {
        log.info("publish input");
        redisTemplate.convertAndSend(topic.getTopic(), message);
        log.info("publish output");
    }

    public void publish_list(ChannelTopic topic, List<ChatRoomRes> chatRoomlist){
        log.info("publish input");
        redisTemplate.convertAndSend(topic.getTopic(), chatRoomlist);
        log.info("publish output");
    }
}