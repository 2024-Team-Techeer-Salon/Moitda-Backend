package com.techeersalon.moitda.domain.chat.repository;

import com.techeersalon.moitda.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    /** ChatMessage 목록조회 - 조건정렬순, ChatRoom 검색 */
    List<ChatMessage> findByMeetingId(Long meetingId);
    Page<ChatMessage> findPagesByMeetingId(Long meetingId, Pageable pageable);
//  Page<ChatMessage> findAll(Pageable pageable);
    /** ChatMessage 검색조회 - 기본정렬순, Message 검색 */
//    List<ChatMessage> findAllByMessageContaining(String message);
//
//    /** ChatMessage 검색조회 - 조건정렬순, Message 검색 */
//    List<ChatMessage> findAllByMessageContaining(String message, Sort sort);
//    /** ChatMessage 목록조회 - 기본정렬순, ChatRoom 검색 */
//    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
