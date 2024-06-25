package com.techeersalon.moitda.domain.meetings.exception.meeting;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MeetingIsFullException extends BusinessException {
    public MeetingIsFullException(){
        super(ErrorCode.MEETING_IS_FULL);
    }
}
