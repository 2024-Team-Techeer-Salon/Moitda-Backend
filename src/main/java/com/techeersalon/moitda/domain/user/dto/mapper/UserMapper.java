package com.techeersalon.moitda.domain.user.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserMeetingRecordRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public RecordsRes toUserMeetingRecord(List<Meeting> userMeetings, List<List<MeetingImage>> images) {
        List<UserMeetingRecordRes> records = IntStream.range(0, userMeetings.size())
                .mapToObj(i -> toEntity(userMeetings.get(i), images.get(i)))
                .collect(Collectors.toList());
        return RecordsRes.builder().userMeetingList(records).build();
    }

    public UserMeetingRecordRes toEntity(Meeting meeting, List<MeetingImage> images) {
        return UserMeetingRecordRes.builder()
                .meetingId(meeting.getId())
                .title(meeting.getTitle())
                .images(images)
                .address(meeting.getAddress())
                .build();
    }
}
