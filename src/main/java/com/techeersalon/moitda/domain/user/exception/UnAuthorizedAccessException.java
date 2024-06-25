package com.techeersalon.moitda.domain.user.exception;

import com.techeersalon.moitda.global.error.exception.BusinessException;

import static com.techeersalon.moitda.global.error.ErrorCode.INVALID_TOKEN_EXCEPTION;

public class UnAuthorizedAccessException extends BusinessException {
    public UnAuthorizedAccessException() {
        super(INVALID_TOKEN_EXCEPTION);
    }
}
