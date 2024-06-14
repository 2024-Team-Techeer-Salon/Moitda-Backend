package com.techeersalon.moitda.domain.chat.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.chat.service.ChatMessageService;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.global.common.SuccessCode;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.techeersalon.moitda.global.common.SuccessCode.*;


@Tag(name = "ChatMessageController", description = "채팅 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/chattings")
public class ChatController {


    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    // 나중에 합쳐야 할 수도?
    /*이건 유저의 채팅방 id 조회를 하는 로직을 저쪽에서 짜면 필요없을 수도*/
    @Operation(description = "ChatRoomList read", summary = "유저의 채팅방 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse> getChatRoomList() {

        List<ChatRoomRes> chatRoomDtos = chatRoomService.getChatRoomsByUser();

        return ResponseEntity.ok(SuccessResponse.of(USER_ROOM_GET_SUCCESS, chatRoomDtos));
    }

    @Operation(description = "GetPagesByRoom", summary = "채팅방의 내역 페이지 조회")
    @GetMapping("/rooms/chatlists/{room_id}")
    public ResponseEntity<SuccessResponse> getPagesByChatRoom(@PathVariable("room_id") Long roomId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        //room_id
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findById(roomId);

        // 채팅방이 존재하는지 확인
        if (chatRoomOptional.isPresent()) {
            List<ChatMessageRes> chatmessages = chatMessageService.getLatestMessageList(roomId, page, size);
            return ResponseEntity.ok(SuccessResponse.of(MESSAGE_GET_SUCCESS, chatmessages));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "", summary = "채팅방에 유저 추가")
    @PatchMapping("/rooms/{room_id}")
    public ResponseEntity<SuccessResponse> addUsertoChatRoom(@PathVariable("room_id") Long roomId, @RequestBody @Valid Long userId) {

        ChatRoomRes chatRoomRes = chatRoomService.addUserToRoom(roomId, userId);
        return ResponseEntity.ok(SuccessResponse.of(USER_APPROVAL_SUCCESS, chatRoomRes));
    }

    @Operation(description = "", summary = "채팅방 유저 삭제")
    @DeleteMapping("/rooms/{room_id}/user")
    public ResponseEntity<SuccessResponse> removeUserFromRoom(@PathVariable("room_id") Long roomId, @RequestBody @Valid Long userId) {

        chatRoomService.removeUserFromRoom(roomId, userId);
        return ResponseEntity.ok(SuccessResponse.of(USER_REMOVAL_SUCCESS));
    }

    /*채팅방 삭제 이때 채팅방 메시지도 동시에 삭제되어야 함*/
    @Operation(description = "ChatRoom delete", summary = "채팅방 삭제")
    @DeleteMapping("/rooms/{room_id}")
    public ResponseEntity<SuccessResponse> deleteChatRoom(@PathVariable("room_id") Long roomId) {
        chatRoomService.deleteRoomAndMessages(roomId);

        return ResponseEntity.ok(SuccessResponse.of(MEETING_DELETE_SUCCESS));
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
