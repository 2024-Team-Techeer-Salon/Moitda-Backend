package com.techeersalon.moitda.config;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // Standard annotation for Spring Boot managed beans
@Slf4j // Import for logging
public class StartConfig implements CommandLineRunner {

    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        //registerMeeting();
    }

    private void registerMeeting() {
        for (int i = 0; i < 105; i++) {


            Meeting meeting = Meeting.builder()
                    .userId(Long.valueOf(i % 15))
                    .categoryId(Long.valueOf(i % 6))
                    .title("title" + i)
                    .participantsCount(1)
                    .maxParticipantsCount(i % 5)
                    .address("building" + i)
                    .addressDetail("1 floor")
                    .buildingName("starbucks" + i)
                    .approvalRequired(true)
                    .appointmentTime(LocalDateTime.now().toString())
                    .build();

            try {
                meetingRepository.save(meeting);
                // Log successful save for debugging purposes
                log.info("Meeting with title '{}' saved successfully.", meeting.getTitle());
            } catch (Exception e) {
                // Log error message if save fails
                log.error("Error saving meeting: {}", e.getMessage());
            }
        }
    }
}
