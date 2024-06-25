package com.techeersalon.moitda.domain.meetings.exception.participant;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class AlreadyParticipatingOrAppliedException extends BusinessException {
    public AlreadyParticipatingOrAppliedException(){
        super(ErrorCode.ALREADY_PARTICIPATING_OR_APPLIED);
    }
}
