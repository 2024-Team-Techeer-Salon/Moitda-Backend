package com.techeersalon.moitda.domain.meetings.dto.mapper;

import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingParticipantListMapper {
    private Long meetingParticipantId;


    public static MeetingParticipantListMapper from(MeetingParticipant meetingParticipant){

        return MeetingParticipantListMapper.builder()
                .meetingParticipantId(meetingParticipant.getId())
                .build();
    }
}
