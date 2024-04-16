package com.techeersalon.moitda.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    //user
    USER_REGISTRATION_SUCCESS(HttpStatus.CREATED, "U001", "회원가입 성공");
    private HttpStatus status;
    private String code;
    private String message;
}
