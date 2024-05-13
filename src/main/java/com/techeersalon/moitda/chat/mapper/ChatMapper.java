package com.techeersalon.moitda.chat.mapper;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;

import com.techeersalon.moitda.chat.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    @Autowired
    private UserService userService;

    public static ChatMessage toChatMessage(User user, Long meetingId, ChatMessageRequestDto request) {
        return ChatMessage.builder()
                .userid(user.getId())
                .meetingId(meetingId)
                .message(request.getMessage())
                .sendDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                //.sendDate(LocalDateTime.from(now()))
                .build();
    }

    public ChatMessageResponseDto toChatMessageDto(ChatMessage chatMessage) {
        Long userId = chatMessage.getUserid();
        // userId를 사용하여 사용자 정보를 조회합니다.
        UserProfileRes userProfile = userService.findUserProfile(userId);

        return ChatMessageResponseDto.builder()
                .userid(chatMessage.getUserid())
                .sender(userProfile.getUsername())
                .profileImage(userProfile.getProfileImage())
                .content(chatMessage.getMessage())
                .sendDate(chatMessage.getSendDate())
                .build();
    }

    public static ChatRoomResponseDto toChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .members(chatRoom.getMembers())
                        //.stream().map(this::toMemberDetail).collect(Collectors.toList()))
                .build();
    }
    public static List<ChatRoomResponseDto> toChatRoomDtoList(List<ChatRoom> rooms) {
        return rooms.stream()
                .map(ChatMapper::toChatRoomDto)
                .collect(Collectors.toList());
    }


    public static List<ChatMessageResponseDto> toChatMessageDtoList(List<ChatMessage> messages) {
        ChatMapper chatMapper = new ChatMapper();;
        return messages.stream()
                .map(chatMapper::toChatMessageDto)
                .collect(Collectors.toList());
    }


//     public static GetLatestMessageListResponseDto of(ChatMessage chatMessage){
//        return GetLatestMessageListResponseDto.builder()
//                .userid(chatMessage.getUserid())
//                .sender(userService.findUserProfile(chatMessage.getUserid()).getUsername())
//                .profileImage(userService.findUserProfile(chatMessage.getUserid()).getProfileImage())
//                .content(chatMessage.getMessage())
//                .sendDate(chatMessage.getSendDate())
//                .build();
//    }
}