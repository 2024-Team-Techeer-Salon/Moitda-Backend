package com.techeersalon.moitda.global.config;

import com.techeersalon.moitda.domain.chat.service.RedisListSubscriber;
import com.techeersalon.moitda.domain.chat.service.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisMessageSubscriber redisSubscriber1,
                                                 RedisListSubscriber redisSubscriber2) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(redisSubscriber1, new RoomIdPatternTopic());
        container.addMessageListener(redisSubscriber2, new MemberIdPatternTopic());
        return container;
    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    class RoomIdPatternTopic extends PatternTopic {
        public RoomIdPatternTopic() {
            super("roomId*");
        }
    }

    class MemberIdPatternTopic extends PatternTopic {
        public MemberIdPatternTopic() {
            super("memberId*");
        }
    }



}
