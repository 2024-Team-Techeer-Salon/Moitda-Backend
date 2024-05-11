package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, Long>, JpaRepository<Meeting, Long>{
    List<Meeting> findByUserId(Long userId);
    //최신순
    Page<Meeting> findAll(Pageable pageable);
}
