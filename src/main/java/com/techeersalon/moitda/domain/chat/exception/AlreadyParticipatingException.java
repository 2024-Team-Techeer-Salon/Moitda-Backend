package com.techeersalon.moitda.domain.chat.exception;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class AlreadyParticipatingException extends BusinessException {
    public AlreadyParticipatingException() {
        super(ErrorCode.ALREADY_PARTICIPATING);
    }
}
