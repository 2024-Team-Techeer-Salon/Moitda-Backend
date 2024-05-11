package com.techeersalon.moitda.chat.mapper;

import com.techeersalon.moitda.chat.domain.ChatMessage;
import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.domain.UserChatRoom;
import com.techeersalon.moitda.chat.dto.ChatMessageRequestDto;
import com.techeersalon.moitda.chat.dto.ChatMessageResponseDto;
import com.techeersalon.moitda.chat.dto.ChatRoomResponseDto;
import com.techeersalon.moitda.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    public UserChatRoom toUserChatRoom(User user, ChatRoom chatRoom) {
        return UserChatRoom.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }

    public ChatMessage toChatMessage(User user, ChatRoom chatRoom, ChatMessageRequestDto request) {
        return ChatMessage.builder()
                .user(user)
                .chatRoom(chatRoom)
                .message(request.getMessage())
                .sendDate(LocalDateTime.from(now()))
                .build();
    }

    public ChatMessageResponseDto toChatMessageDto(ChatMessage chatMessage) {
        return ChatMessageResponseDto.builder()
                .sender(chatMessage.getUser().getUsername())
                .content(chatMessage.getMessage())
                .sendDate(chatMessage.getSendDate())
                .build();
    }

    public ChatRoomResponseDto toChatRoomDto(ChatRoom chatRoom, List<User> otherUsers) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .roomName(chatRoom.getName())
                .members(otherUsers.stream().map(this::toMemberDetail).collect(Collectors.toList()))
                .build();
    }

    private ChatRoomResponseDto.MemberDetail toMemberDetail(User user) {
        return ChatRoomResponseDto.MemberDetail.builder()
                .id(user.getId())
                .name(user.getUsername())
                .bannerImage(user.getBannerImage())
                .build();
    }


        public List<ChatMessageResponseDto> toChatMessageList(List<ChatMessage> messages) {
            return messages.stream().map(this::toChatMessageDto).collect(Collectors.toList());
        }
}