package com.techeersalon.moitda.domain.chat.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.request.ChatMessageReq;
import com.techeersalon.moitda.domain.chat.service.ChatMessageService;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {
    @Autowired
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ChatMapper chatMapper;

//    @EventListener
//    public void handleWebSocketConnectListener(StompHeaderAccessor headerAccessor) {
//        headerAccessor.
//        log.info("Received a new web socket connection");
//    }

//    @MessageMapping("")
//    public void handleStompMessage(StompHeaderAccessor accessor) {
//        // STOMP 메시지의 헤더에 액세스할 수 있습니다.
//        accessor.getMessage();
//        String sessionId = accessor.getSessionId();
//        String destination = accessor.getDestination();
//
//        // 추가적인 처리를 수행할 수 있습니다.
//    }
    @MessageMapping(value = "/chat/room/{roomId}")
    @SendToUser("/sub/chat/room/{roomId}")
    public void send_message(StompHeaderAccessor headerAccessor,
                             @DestinationVariable String roomId,
                             @Payload ChatMessageReq messageDto) {
        List<String> authorizationHeaders = headerAccessor.getNativeHeader("Authorization");
        String jwtToken = authorizationHeaders.get(0).replace("Bearer ", "");
        Object[] objects = jwtService.extractEmailAndSocialType(jwtToken);
        SocialType socialType = (SocialType) objects[1];
        String email = (String) objects[0];
        User user = userRepository.findBySocialTypeAndEmail(socialType, email).get();

        log.info("# roomId = {}", roomId);
        ChatMessage chatMessage = ChatMapper.toChatMessage(user, Long.valueOf(roomId), messageDto);
        ChatMessageRes responseDto = chatMapper.toChatMessageDto(chatMessage);
            /* 채팅방에 유저 추가하는 것만 하면 될 듯*/
        template.convertAndSend("/sub/chat/room/" + roomId,responseDto); /*채팅방으로*/
        /*채팅 저장*/
        chatMessageService.save(user, Long.valueOf(roomId), messageDto);
        log.info("pub success " + messageDto.getMessage());
        //return;
    }

//    @MessageMapping(value = "/room/founder/{memberId}")
//    public void invite(@DestinationVariable String memberId, ChatRoomInviteDto inviteDto) {
////        Member member = new Member();
//        long parsedmemberId = Long.parseLong(memberId);
//        RoomInfoResponseDto responseDto = chatRoomService.createRoom(inviteDto.getusername(), parsedmemberId, inviteDto.user_Id(), inviteDto.getTitle());/*채팅방 개설*/
//        log.info("채팅방 개설 및 조회 성공");
//        template.convertAndSend("/sub/room/founder/" + memberId, responseDto);
//
//        roomService.inviteRoom(parsedmemberId, responseDto.getRoomInfoId(), inviteDto);/*나와 상대를 개설 채팅방으로 입장*/
//
//    }



}
