package com.techeersalon.moitda.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String code;
    private String errorMessage;

    public ErrorResponse(HttpStatus status, String s) {
        this.errorMessage = s;
        this.httpStatus = status;
    }

    public ErrorResponse(ErrorCode code) {
        this.errorMessage = code.getMessage();
        this.httpStatus = code.getStatus();
        this.code = code.getCode();
    }
}