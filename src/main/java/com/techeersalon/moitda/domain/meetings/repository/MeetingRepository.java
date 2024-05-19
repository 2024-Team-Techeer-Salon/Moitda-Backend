package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, Long>, JpaRepository<Meeting, Long>{
    List<Meeting> findByUserId(Long userId);
    //List<Meeting> getUserMeetingList();

    //최신순
    Page<Meeting> findAll(Pageable pageable);
    Page<Meeting> findByUserId(Long userId, Pageable pageable);
    Optional<Meeting> findById(Long id);

    List<Meeting> findByIdIn(List<Long> ids);
}
