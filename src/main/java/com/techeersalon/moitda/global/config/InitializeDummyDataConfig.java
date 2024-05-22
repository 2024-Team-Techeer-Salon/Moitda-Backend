package com.techeersalon.moitda.global.config;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.entity.Gender;
import com.techeersalon.moitda.domain.user.entity.Role;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Component // Standard annotation for Spring Boot managed beans
@Slf4j // Import for logging
public class InitializeDummyDataConfig implements CommandLineRunner {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingParticipantRepository meetingParticipantRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        registerUser();
        registerMeeting();
    }

    private void registerUser(){
        int userCount = 6;
        for(int i = 1; i <= userCount; i++){
            User user = User.builder()
                    .username("name" + i)
                    .email("userEmail" + i + "@naver.com")
                    .profileImage("profileImage")
                    .bannerImage("bannerImage")
                    .dataOfBirth(LocalDate.from(LocalDateTime.now()
                            .minusYears(20+i)
                            .withMonth(i)
                            .withDayOfMonth(i)))
                    .gender(Gender.M)
                    .mannerStat(50)
                    //.location()
                    .role(Role.USER)
                    .socialType(SocialType.NAVER)
                    .build();
            userRepository.save(user);

        }

    }
    private void registerMeeting() {
        int meetingCount = 150;
        String[] address = {"경남 창원시 마산회원구 석전동 223-4"
                ,"서울 강동구 성내동 550"
                ,"서울 강서구 화곡동 772-67"
                ,"인천 강화군 강화읍 갑곳리 203-10"
                ,"경기 광주시 곤지암읍 삼리 399-6"
                ,"경기 구리시 토평동 566-6"
                ,"경북 구미시 송정동 33-11"
                ,"경북 김천시 부곡동 415-1"
                ,"인천 남동구 간석동 380-4"
                ,"인천 남동구 간석동 772"
                ,"경기 동두천시 지행동 475-1"
                ,"부산 동래구 온천동 1428-36"
                ,"경북 문경시 문경읍 상초리 288-33"
                ,"경기 부천시 원미구 춘의동 168-3"
                };
        Random random = new Random();

        for (int i = 0; i < meetingCount; i++) {
            User user = userRepository.findById((long) (random.nextInt(6) + 1)).get();
            Meeting meeting = Meeting.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .categoryId(Long.valueOf(random.nextInt(19)))
                    .title("title" + i)
                    .participantsCount(1)
                    .maxParticipantsCount(random.nextInt(30) + 1)
                    .roadAddressName(address[random.nextInt(address.length)])
                    .placeName("스타벅스")
                    .approvalRequired(true)
                    .appointmentTime(LocalDateTime.now()
                                    .plusDays(random.nextInt(30) + 1)
                                    .withHour(random.nextInt(10)+12)
                                    .withMinute(0)
                                    .toString()
                    )
                    .build();



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
