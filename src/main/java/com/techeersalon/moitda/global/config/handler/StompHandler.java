package com.techeersalon.moitda.global.config.handler;

import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    @Autowired
    private final JwtService tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Unauthorized attempt to connect without a valid authorization header");
                return (Message<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid authorization header provided");
            }
            String token = authHeader.substring(7);
            if (token == null || !tokenProvider.isTokenValid(token)) {
                log.warn("Unauthorized attempt to connect with an invalid token");
                return (Message<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
            }
//            User principal = (User) authentication.getPrincipal();
//            String email = principal.getUsername();
//            Member member = memberRepository.findByEmail(email);
//            accessor.setUser(authentication);
        }
        return message;
    }

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        log.info("여기까진 들어옴");
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        // websocket 연결시 헤더의 jwt token 유효성 검증
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//            final String authorization = tokenProvider.extractJwt(accessor);
//            if(tokenProvider.isTokenValid(authorization)==true) {
//                //chatRoomService.addUserToChatRoom(chatRoomId, userId);
//            }
//        }
//        return message;
//    }
////

//    @Override
//    public void postSend(Message message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        StompCommand command = accessor.getCommand();
//        // unsubscribe 상태 감지
//        if (command == UNSUBSCRIBE) {
//            List<String> disconnectedMemberId = accessor.getNativeHeader("memberId");
//            List<String> disconnectedRoomId = accessor.getNativeHeader("roomId");
//            // 채팅방 unsubscribe 상태 감지
//            if (disconnectedMemberId != null && disconnectedRoomId != null) {
//                Long userId = Long.parseLong(disconnectedMemberId.get(0));
//                Long roomId = Long.parseLong(disconnectedRoomId.get(0));
//
//                //chatroomService.updateLastReadChat(roomId, userId);
//                log.info("user #" + userId + " leave chat room #" + roomId);
//            }
//        }
//    }
}