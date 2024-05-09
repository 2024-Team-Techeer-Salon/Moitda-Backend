package com.techeersalon.moitda.chat.config.handler;

import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoom;
import com.techeersalon.moitda.chat.service.ChatRoomService;
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

import java.util.List;

import static org.springframework.messaging.simp.stomp.StompCommand.UNSUBSCRIBE;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
//    private final testTokenProvider tokenProvider;

    private final ChatRoomService chatroomService;

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();
        // unsubscribe 상태 감지
        if (command == UNSUBSCRIBE) {
            List<String> disconnectedMemberId = accessor.getNativeHeader("memberId");
            List<String> disconnectedRoomId = accessor.getNativeHeader("roomId");
            // 채팅방 unsubscribe 상태 감지
            if (disconnectedMemberId != null && disconnectedRoomId != null) {
                Long userId = Long.parseLong(disconnectedMemberId.get(0));
                Long roomId = Long.parseLong(disconnectedRoomId.get(0));

                chatroomService.updateLastReadChat(roomId, userId);
                log.info("user #" + userId + " leave chat room #" + roomId);
            }
        }
    }
}