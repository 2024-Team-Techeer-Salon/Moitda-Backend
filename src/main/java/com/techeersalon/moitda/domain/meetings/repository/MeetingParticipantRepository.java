package com.techeersalon.moitda.domain.meetings.repository;

import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    List<MeetingParticipant> findByMeetingIdAndIsWaiting(Long MeetingId, Boolean bool);

    Boolean existsByMeetingIdAndUserId(Long meetingId, Long UserId);

    List<MeetingParticipant> findByUserIdAndIsWaiting(Long UserId, Boolean bool);
}
