package com.techeersalon.moitda.domain.chat.controller;

import com.techeersalon.moitda.domain.chat.service.ChatMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Component
public class WebSocketEventListener {
    private static final Logger logger = Logger.getLogger(WebSocketEventListener.class.getName());
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService messageService;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, ChatMessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        LocalDateTime disconnectTime = LocalDateTime.now();

        logger.info("User Disconnected: " + sessionId + " at " + disconnectTime);
        // Save the disconnect time to a database or any storage
        messageService.updateLastReadChat(sessionId, disconnectTime);
    }

}
