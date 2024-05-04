package com.techeersalon.moitda.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
//    private final testTokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결시 헤더의 jwt token 유효성 검증
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//            final String authorization = tokenProvider.extractJwt(accessor);
//            tokenProvider.validateToken(authorization);
//        }
        return message;
    }

    /*
    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        switch ((accessor.getCommand())) {
            case CONNECT:

                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
                log.info("세션 들어옴 => {}", sessionId);
                break;

            case DISCONNECT:

                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생
                log.info("세션 끊음 => {}", sessionId);
                break;

            default:

                break;
        }

    }*/
}