package com.techeersalon.moitda.domain.meetings.exception.participant;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MeetingParticipantNotFoundException extends BusinessException {
    public MeetingParticipantNotFoundException(){
        super(ErrorCode.PARTICIPANT_NOT_FOUND);
    }
}
