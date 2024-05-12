package com.techeersalon.moitda.chat.repository;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.chat.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    List<UserChatRoom> findByUserId(Long userId);
    List<UserChatRoom> findByChatRoom(ChatRoom chatRoom);

}