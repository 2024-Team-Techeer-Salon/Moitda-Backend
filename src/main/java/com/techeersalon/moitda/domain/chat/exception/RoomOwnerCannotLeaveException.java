package com.techeersalon.moitda.domain.chat.exception;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class RoomOwnerCannotLeaveException extends BusinessException {
    public RoomOwnerCannotLeaveException() {
        super(ErrorCode.ROOM_OWNER_CANNOT_LEAVE);
    }
}
