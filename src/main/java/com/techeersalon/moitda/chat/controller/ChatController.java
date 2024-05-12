package com.techeersalon.moitda.chat.controller;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.chat.dto.ChatRoomRequestDto;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import com.techeersalon.moitda.chat.service.*;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@Tag(name = "ChatMessageController", description = "채팅 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/chattings")
public class ChatController {


    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final JwtService tokenProvider;

    @Operation(summary = "chatroom create", description = "채팅방 생성")
    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String authHeader, @RequestBody ChatRoomRequestDto roomRequestDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No token provided");
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findBySocialTypeAndEmail(
                SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername());
        User user = optionalUser.get();
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }

        String RoomName = roomRequestDto.getRoomName();
        if (RoomName == null || RoomName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Chat room name is required");
        }


        ChatRoom chatRoom = chatRoomService.createChatRoom(RoomName); // 서비스 계층에 이름을 전달
        chatRoomService.createUserChatRoom(user, chatRoom.getId());
        //chatRoomService.addUserToRoom(chatRoom.getId(), user.getId());

        ChatRoomResponseDto chatRoomRes = ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .roomName(RoomName)// 채팅방 이름 포함
                //.members()
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomRes);
    }
    // 나중에 합쳐야 할 수도?

    //@Transactional
    @Operation(summary = "ChatRoomList read", description = "채팅방 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRoomList(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findBySocialTypeAndEmail(
                SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername());
        User user = optionalUser.get();


        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findUserChatRoomByUserId(user.getId());
        return ResponseEntity.ok(chatRoomList);
    }


    @Operation(summary = "GetMessagesByRoom", description = "채팅방의 채팅 내역 조회")
    @GetMapping("/rooms/chatmessages/{room_id}")
    public ResponseEntity<?> getChatMessagesByChatRoom(@PathVariable Long room_id) {
        //room_id
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findById(room_id);

        // 채팅방이 존재하는지 확인
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            List<ChatMessageResponseDto> chatmessages = chatMessageService.findChatMessage(chatRoom);
            return ResponseEntity.ok().body(chatmessages);
        }
        return ResponseEntity.notFound().build();
    }

//    @Operation(summary = "GetRoomByUser", description = "유저의 채팅방 조회")
//

}






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
