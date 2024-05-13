package com.techeersalon.moitda.domain.user.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserMeetingRecordRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserProfileRes toUserProfile(User user) {
        return UserProfileRes.builder()
                .username(user.getUsername())
                .mannerStat(user.getMannerStat())
                .profileImage(user.getProfileImage())
                .bannerImage(user.getBannerImage())
                .gender(user.getGender())
                .introduce(user.getIntroduce())
                .location(user.getLocation())
                .build();
    }

    public RecordsRes toUserMeetingRecord(List<Meeting> userMeetings) {
        List<UserMeetingRecordRes> records = userMeetings.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        return RecordsRes.builder().userMeetingList(records).build();
    }

    public UserMeetingRecordRes toEntity(Meeting meeting) {
        return UserMeetingRecordRes.builder()
                .meetingId(meeting.getId())
                .title(meeting.getTitle())
                .image(meeting.getImage())
                .address(meeting.getAddress())
                .build();
    }
}
