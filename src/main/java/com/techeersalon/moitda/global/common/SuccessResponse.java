package com.techeersalon.moitda.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse {
    private String message;
    private HttpStatus httpStatus;
    private String code;

    public SuccessResponse(HttpStatus status, String s) {
        this.message = s;
        this.httpStatus = status;
    }

    public SuccessResponse(SuccessCode code) {
        this.message = code.getMessage();
        this.httpStatus = code.getStatus();
        this.code = code.getCode();
    }
}
