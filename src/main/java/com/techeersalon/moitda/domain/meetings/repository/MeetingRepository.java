package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByUserId(Long userId);
    //최신순
    List<Meeting> findByCreatedAtContains(Pageable pageable);
}
