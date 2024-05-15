package com.techeersalon.moitda.domain.meetings.exception.participant;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class NotAuthorizedToAppproveException extends BusinessException {
    public NotAuthorizedToAppproveException(){
        super(ErrorCode.NOT_AUTHORIZED_TO_APPROVE);
    }
}
