package com.techeersalon.moitda.domain.meetings.repository;


import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}