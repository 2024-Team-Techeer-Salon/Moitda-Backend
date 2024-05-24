package com.techeersalon.moitda.domain.chat.exception;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class MessageNotFoundException extends BusinessException {
    public MessageNotFoundException(){
        super(ErrorCode.MESSAGE_NOT_FOUND);
    }
}
