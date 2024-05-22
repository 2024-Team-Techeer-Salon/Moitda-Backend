package com.techeersalon.moitda.domain.chat.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.chat.service.ChatMessageService;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.common.SuccessCode;
import com.techeersalon.moitda.global.common.SuccessResponse;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    // 나중에 합쳐야 할 수도?
    /*이건 유저의 채팅방 id 조회를 하는 로직을 저쪽에서 짜면 필요없을 수도*/
    //@Transactional
    @Operation( description = "ChatRoomList read", summary= "유저의 채팅방 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse> getChatRoomList(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findBySocialTypeAndEmail(
                SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername());
        User user = optionalUser.get();

        List<ChatRoomRes> chatRoomDtos = chatRoomService.getChatRoomsByUser(user);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.USER_ROOM_GET_SUCCESS,chatRoomDtos));
    }

    /*채팅방의 채팅 내역 조회*/
    @Operation(description = "GetMessagesByRoom", summary = "채팅방의 채팅 내역 조회")
    @GetMapping("/rooms/chatmessages/{room_id}")
    public ResponseEntity<SuccessResponse> getChatMessagesByChatRoom(@PathVariable("room_id") Long roomid) {
        //room_id
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findById(roomid);

        // 채팅방이 존재하는지 확인
        if (chatRoomOptional.isPresent()) {
            List<ChatMessageRes> chatmessages = chatMessageService.findChatMessage(roomid);
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.MESSAGE_GET_SUCCESS,chatmessages));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "",summary="채팅방에 유저 추가")
    @PatchMapping("/rooms/{room_id}")
    public ResponseEntity<SuccessResponse> addUsertoChatRoom(@PathVariable("room_id") Long roomid){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findBySocialTypeAndEmail(
                SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername());
        User user = optionalUser.get();

        ChatRoomRes chatRoomRes = chatRoomService.addUserToRoom(roomid,user.getId());
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.USER_APPROVAL_SUCCESS, chatRoomRes));
    }

    /*채팅방 삭제 이때 채팅방 메시지도 동시에 삭제되어야 함*/
    @Operation(description="ChatRoom delete", summary = "채팅방 삭제")
    @DeleteMapping("/rooms/{room_id}")
    public ResponseEntity<SuccessResponse> deleteChatRoom(@PathVariable("room_id") Long roomid){
        chatRoomService.deleteRoomAndMessages(roomid);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.MEETING_DELETE_SUCCESS));
    }

//    @Operation(summary = "GetRoomByUser", description = "유저의 채팅방 조회")
//

}







    // 사용자가 웹 소켓 연결을 끊으면 실행됨
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
