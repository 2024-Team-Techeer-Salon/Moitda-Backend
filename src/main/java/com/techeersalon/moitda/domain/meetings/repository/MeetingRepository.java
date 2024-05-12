package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByUserId(Long userId);

    Optional<Meeting> findById(Long id);
}
