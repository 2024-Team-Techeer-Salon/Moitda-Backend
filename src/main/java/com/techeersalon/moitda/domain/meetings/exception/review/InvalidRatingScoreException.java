package com.techeersalon.moitda.domain.meetings.exception.review;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class InvalidRatingScoreException extends BusinessException {
    public InvalidRatingScoreException() {
        super(ErrorCode.INVALID_RATING_SCORE);
    }
}
