package com.techeersalon.moitda.chat.repository;

import com.techeersalon.moitda.chat.domain.ChatRoom;
import com.techeersalon.moitda.domain.user.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    /** ChatRoom 조회 - RoomName 검색, 정확히 일치 */
    //ChatRoom findByName(String roomName);
    //List<ChatRoom> getChatRoomsBy(User user);

    List<ChatRoom> findByMembers(User user);

//    List<ChatRoom> findAllByNameContaining(String name, Sort sort);

//    /** ChatRoom 목록조회 - 기본정렬순, RoomName 검색, 정확히 일치 */
//    List<ChatRoom> findAllByRoomName(String roomName);
//
//    /** ChatRoom 목록조회 - 조건정렬순, RoomName 검색, 정확히 일치 */
//    List<ChatRoom> findAllByRoomName(String roomName, Sort sort);
//
//    /** ChatRoom 목록조회 - 기본정렬순, RoomName 검색, 포함 일치 */
//    List<ChatRoom> findAllByRoomNameContaining(String roomName);

    /** ChatRoom 목록조회 - 조건정렬순, RoomName 검색, 포함 일치 */
//    List<UserChatRoom> findAllByRoomNameContaining(String roomName, Sort sort);
}
