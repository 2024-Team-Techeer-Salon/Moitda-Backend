package com.techeersalon.moitda.domain.chat.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Component
public class WebSocketEventListener {
    private static final Logger logger = Logger.getLogger(WebSocketEventListener.class.getName());

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        LocalDateTime disconnectTime = LocalDateTime.now();

        logger.info("User Disconnected: " + sessionId + " at " + disconnectTime);

        // Save the disconnect time to a database or any storage
        saveDisconnectTime(sessionId, disconnectTime);
    }

    private void saveDisconnectTime(String sessionId, LocalDateTime disconnectTime) {
        // Implement your logic to save the disconnect time
        // For example, you can use a service to save it to a database
    }
}
