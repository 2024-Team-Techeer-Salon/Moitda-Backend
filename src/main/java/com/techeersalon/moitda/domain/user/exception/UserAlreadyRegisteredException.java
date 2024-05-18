package com.techeersalon.moitda.domain.user.exception;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class UserAlreadyRegisteredException extends BusinessException {
    public UserAlreadyRegisteredException() {
        super(ErrorCode.USER_ALREADY_REGISTERED);
    }
}
