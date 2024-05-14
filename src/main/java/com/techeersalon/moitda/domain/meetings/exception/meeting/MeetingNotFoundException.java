package com.techeersalon.moitda.domain.meetings.exception.meeting;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MeetingNotFoundException extends BusinessException {
    public MeetingNotFoundException(){
        super(ErrorCode.MEETING_MAX_PARTICIPANTS_NOT_EXCEEDED);
    }
}
