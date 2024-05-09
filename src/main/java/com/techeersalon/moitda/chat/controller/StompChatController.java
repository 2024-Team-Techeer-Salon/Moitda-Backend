package com.techeersalon.moitda.chat.controller;

import com.techeersalon.moitda.chat.domain.chatMessage.ChatMessage;
import com.techeersalon.moitda.chat.repository.ChatMessageRepository;
import com.techeersalon.moitda.chat.domain.chatMessage.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.service.ChatMessageService;
import com.techeersalon.moitda.chat.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatRepository;

    @MessageMapping(value = "/chat/room/{room_Id}")
    public void send_message(@DestinationVariable String room_Id, ChatMessageRequestDto messageDto) {
        log.info("# roomId = {}", messageDto.getRoomId());
        if (ChatMessage.MessageType.ENTER.equals(messageDto.getMessageType())){
            messageDto.setMessage(messageDto.getUserId() + "님이 입장하셨습니다.");
            /* 채팅방에 유저 추가하는 것만 하면 될 듯*/
        }
        template.convertAndSend("/sub/chat/room/" + room_Id, messageDto); /*채팅방으로*/
        /*채팅 저장*/
        chatMessageService.save(Long.parseLong(room_Id), messageDto);
        log.info("pub success" + messageDto.getMessage());
        return;
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
