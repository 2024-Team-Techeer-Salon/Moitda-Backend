package com.techeersalon.moitda.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse {
    private String code;
    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public static SuccessResponse of(SuccessCode successCode){
        return new SuccessResponse(successCode, "");
    }
    public static SuccessResponse of(SuccessCode successCode, Object data) {
        return new SuccessResponse(successCode, data);
    }

    public SuccessResponse(SuccessCode code, Object data) {
        this.code = code.getCode();
        this.httpStatus = code.getStatus();
        this.message = code.getMessage();
        this.data = data;
    }
}
