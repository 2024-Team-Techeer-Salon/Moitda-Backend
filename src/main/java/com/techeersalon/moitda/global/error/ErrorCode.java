package com.techeersalon.moitda.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "서버 오류"),

    // 유저
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "U001", "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN_EXCEPTION (HttpStatus.UNAUTHORIZED, "U002", "토큰이 유효하지 않습니다.");

    private HttpStatus status;
    private String code;
    private String message;
}
