package com.techeersalon.moitda.config;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
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

    @Autowired
    private MeetingParticipantRepository meetingParticipantRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        //registerMeeting();
    }

    private void registerMeeting() {
        for (int i = 0; i < 105; i++) {


            Meeting meeting = Meeting.builder()
                    .userId(Long.valueOf(1))
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

//            CreateMeetingRequest createMeeting = new CreateMeetingRequest((long)i % 6,"title" + i,"building" + i ,
//                    "1 floor","starbucks" + i, (i % 5)+2, true, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString());


            try {
                meetingRepository.save(meeting);
                // Log successful save for debugging purposes
                //Long meetingId= meetingService.addMeeting(createMeeting);
                log.info("Meeting with title '{}' saved successfully.", meeting.getTitle());
            } catch (Exception e) {
                // Log error message if save fails
                log.error("Error saving meeting: {}", e.getMessage());
            }

            MeetingParticipant participant = MeetingParticipant.builder()
                    .meetingId(meeting.getId())
                    .userId(meeting.getUserId())
                    .isWaiting(false)
                    .build();

            meetingParticipantRepository.save(participant);
        }
    }
}
