package com.techeersalon.moitda.global.s3.exception;

import com.techeersalon.moitda.global.error.ErrorCode;
import com.techeersalon.moitda.global.error.exception.BusinessException;

public class S3Exception extends BusinessException {

    public S3Exception() {
        super(ErrorCode.PUT_OBJECT_EXCEPTION);
    }
}
