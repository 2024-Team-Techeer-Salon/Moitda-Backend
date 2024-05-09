package com.techeersalon.moitda.chat.controller;

import com.techeersalon.moitda.chat.domain.chatMessage.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoomRequestDto;
import com.techeersalon.moitda.chat.domain.chatRoom.ChatRoomResponseDto;
import com.techeersalon.moitda.chat.service.*;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@Tag(name = "ChatMessageController", description = "채팅 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/chattings")
public class ChatController {


    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    @Operation(summary = "createMeeting", description = "채팅방 생성")
    @PostMapping
    public ResponseEntity<String> CreateChatRoom(@Validated @RequestBody ChatRoomRequestDto chatRoomDto) {
        chatRoomService.save(chatRoomDto);
        return ResponseEntity.created(URI.create("/chat/")).body("채팅방 생성");
    }
    // 나중에 합쳐야 할 수도?

    @Operation(summary = "GetRoomByUser", description = "유저의 채팅방 조회")
    //@GetMapping("/rooms/{user_id}")
    @GetMapping("/rooms/")
    public ResponseEntity<?> getChatRoomsByUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername()).get();

        List<ChatRoomResponseDto> chatRooms = chatRoomService.findALlDesc(user.getId());
        return ResponseEntity.ok().body(chatRooms);
    }

    @Operation(summary = "GetMessagesByRoom", description = "채팅방의 채팅 내역 조회")
    @GetMapping("/rooms/chatmessages/{room_id}")
    public ResponseEntity<?> getChatMessagesByChatRoom(@PathVariable Long room_id) {
        List<ChatMessageResponseDto> chatmessages = chatMessageService.findAllByChatRoomIdDesc(room_id);
        return ResponseEntity.ok().body(chatmessages);
    }

//    @Operation(summary = "GetRoomByUser", description = "유저의 채팅방 조회")
//

}





//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        log.info("Received a new web socket connection");
//    }
//
//    // 사용자가 웹 소켓 연결을 끊으면 실행됨
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
//        String sessionId = headerAccesor.getSessionId();
//
//        log.info("sessionId Disconnected : " + sessionId);
//    }
//    @MessageMapping("/chat/message/{roomId}")
//    @SendTo("/sub/chat/room/{roomId}")
//    public void send_message(ChatMessageDto message) {
//        log.info("# roomId = {}", message.getChatRoomId());
//        if (ChatMessageDto.MessageType.ENTER.equals(message.getMessageType())){
//            message.setMessage(message.getSenderId() + "님이 입장하셨습니다.");
//        }
//
//        log.info("# Sending message to /sub/chat/room/{}: {}", message.getChatRoomId(), message);
//
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
//
//        log.info("# Message sent to /sub/chat/room/{}", message.getChatRoomId());
//    }
