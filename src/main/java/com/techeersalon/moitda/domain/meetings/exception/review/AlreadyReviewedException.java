package com.techeersalon.moitda.domain.meetings.exception.review;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class AlreadyReviewedException extends BusinessException {
    public AlreadyReviewedException() {
        super(ErrorCode.ALREADY_REVIEWED);
    }
}
