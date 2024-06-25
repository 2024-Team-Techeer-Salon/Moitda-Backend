package com.techeersalon.moitda.domain.meetings.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantListMapper {
    private Long userId;
    private String username;
    private String profileImage;
    private Boolean isWaiting;

    public static MeetingParticipantListMapper from(MeetingParticipant meetingParticipant, User user) {

        return MeetingParticipantListMapper.builder()
                .userId(meetingParticipant.getUserId())
                .username(meetingParticipant.getUsername())
                .profileImage(user.getProfileImage())
                .isWaiting(meetingParticipant.getIsWaiting())
                .build();
    }
}
