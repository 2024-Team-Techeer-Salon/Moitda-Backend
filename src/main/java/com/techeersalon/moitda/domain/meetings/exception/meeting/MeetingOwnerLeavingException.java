package com.techeersalon.moitda.domain.meetings.exception.meeting;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MeetingOwnerLeavingException extends BusinessException {
    public MeetingOwnerLeavingException() {
        super(ErrorCode.MEETING_OWNER_LEAVING_EXCEPTION);
    }
}
