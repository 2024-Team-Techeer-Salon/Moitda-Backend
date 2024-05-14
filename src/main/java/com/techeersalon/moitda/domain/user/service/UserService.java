package com.techeersalon.moitda.domain.user.service;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UserNotFoundException;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final UserMapper userMapper;

    public void signup(SignUpReq signUpReq) {
//        수정필요
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        user.signupUser(signUpReq);

        userRepository.save(user);
    }

    public void logout() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        user.onLogout();

        userRepository.save(user);
    }


    public UserProfileRes findUserProfile(Long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toUserProfile(user);
    }

    public void updateUserProfile(UpdateUserReq updateUserReq) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        user.updateProfile(updateUserReq);

        userRepository.save(user);
    }

    public User getLoginUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loginUser = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return loginUser;
    }

    public RecordsRes getUserMeetingRecords(Long userId) {

        if (userRepository.existsById(userId)) {
            List<MeetingParticipant> meetingParticipantList =
                    meetingParticipantRepository.findByUserIdAndIsWaiting(userId, false);
            List<Long> meetingIds = meetingParticipantList
                    .stream()
                    .map(MeetingParticipant::getMeetingId)
                    .collect(Collectors.toList());
            List<Meeting> userMeetings = meetingRepository.findByIdIn(meetingIds);

            return userMapper.toUserMeetingRecord(userMeetings);
        }

        throw new UserNotFoundException();
    }
}