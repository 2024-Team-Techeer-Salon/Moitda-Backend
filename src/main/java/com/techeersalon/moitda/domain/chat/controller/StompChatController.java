package com.techeersalon.moitda.domain.chat.controller;

import com.techeersalon.moitda.domain.chat.dto.request.ChatRoomReq;
import com.techeersalon.moitda.domain.chat.dto.response.ChatRoomRes;
import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import com.techeersalon.moitda.domain.chat.dto.response.ChatMessageRes;
import com.techeersalon.moitda.domain.chat.dto.mapper.ChatMapper;
import com.techeersalon.moitda.domain.chat.dto.request.ChatMessageReq;
import com.techeersalon.moitda.domain.chat.service.ChatMessageService;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.domain.chat.service.RedisPublisher;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.domain.user.service.UserService;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
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
    private final RedisPublisher redisPub;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    /*사용자 채팅 리스트 불러오기*/
    @MessageMapping(value = "/room/{memberId}")
    public void enter(StompHeaderAccessor headerAccessor,
                      @DestinationVariable String memberId
                      ) {
        List<String> authorizationHeaders = headerAccessor.getNativeHeader("Authorization");
        String jwtToken = authorizationHeaders.get(0).replace("Bearer ", "");
        Object[] objects = jwtService.extractEmailAndSocialType(jwtToken);
        SocialType socialType = (SocialType) objects[1];
        String email = (String) objects[0];
        User user = userRepository.findBySocialTypeAndEmail(socialType, email).get();
        List<ChatRoomRes> chatRoomDtos = chatRoomService.getChatRoomsByUser(user);
        log.info("room list");
        redisPub.publish_list(ChannelTopic.of("memberId"+memberId), chatRoomDtos);
    }

    /*Message 보내기*/
    @MessageMapping(value = "/chat/room/{roomId}")
//    @SendTo("/sub/chat/room/{roomId}")
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
        /*MessageType이 Enter = 즉 User가 채팅방에 새로 들어왔을 */
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getMessageType()))
            log.info("/sub/chat/room/" + roomId, LocalDateTime.now());

        ChatMessageRes chatMessageRes = chatMessageService.createChatMessage(user, Long.valueOf(roomId), messageDto);
        log.info("message create");
        /* 채팅방에 유저 추가하는 것만 하면 될 듯*/
        redisPub.publish(ChannelTopic.of("roomId"+roomId),chatMessageRes); /*채팅방으로*/
        log.info("pub success " + messageDto.getMessage());
        /*채팅 저장*/

    }


}
