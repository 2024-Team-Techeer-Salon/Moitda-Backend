package com.techeersalon.moitda.domain.meetings.exception.review;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MeetingNotEndedException extends BusinessException {
    public MeetingNotEndedException() {
        super(ErrorCode.MEETING_NOT_ENDED);
    }
}
