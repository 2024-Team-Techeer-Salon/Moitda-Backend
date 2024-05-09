package com.techeersalon.moitda.domain.meetings.repository;

import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    public List<MeetingParticipant> findByMeetingId(Long MeetingId);
}
