package com.techeersalon.moitda.domain.meetings.dto.response;

import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMeetingDetailResponse {
    private String title;

    private Long user_id;

    private Long catagory_id;

    private String content;

    private Integer max_participants_count;

    private Integer participants_count;

    //참여자 리스트 줘야 되는데.. 수정필요
    private String image_url;

    private String createdAt;

    public static GetMeetingDetailResponse of(Meeting meeting) {
        return GetMeetingDetailResponse.builder()
                .title(meeting.getTitle())
                .user_id(meeting.getUser_id())
                .catagory_id(meeting.getCategory_id())
                .content(meeting.getContent())
                .max_participants_count(meeting.getMaxParticipantsCount())
                .participants_count(meeting.getParticipantsCount())
                .image_url(meeting.getImage())
                .createdAt(meeting.getCreateAt())
                .build();
    }
}