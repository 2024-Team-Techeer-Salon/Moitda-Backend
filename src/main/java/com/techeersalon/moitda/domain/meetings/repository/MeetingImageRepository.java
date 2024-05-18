package com.techeersalon.moitda.domain.meetings.repository;

import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MeetingImageRepository extends JpaRepository<MeetingImage, Long> {

    List<MeetingImage> findByMeetingId(Long meetingId);

    void deleteByMeetingId(Long meetingId);
}
