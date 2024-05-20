package com.techeersalon.moitda.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "서버 오류"),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "G002", "이미지 업로드 중 에러가 발생했습니다. 잠시 후 다시 시도해 주세요."),

    // 유저
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "U001", "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN_EXCEPTION (HttpStatus.UNAUTHORIZED, "U002", "토큰이 유효하지 않습니다."),
    USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "U003", "이미 회원가입된 사용자입니다."),

    // 모임
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND,"M001","미팅을 찾을 수 없습니다."),
    MEETING_MAX_PARTICIPANTS_NOT_EXCEEDED(HttpStatus.BAD_REQUEST,"M002","최대 참가 인원 수가 너무 적습니다."),
    MEETING_PAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"M003","미팅 페이지를 찾을 수 없습니다."),
    MEETING_IS_FULL(HttpStatus.BAD_REQUEST,"M004","참여자가 가득 찼습니다."),

    // 참여자
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND,"P001","참여자을 찾을 수 없습니다."),
    ALREADY_PARTICIPATING_OR_APPLIED(HttpStatus.NOT_FOUND,"P002","이미 모임에 신청한 상태 거나 참가자 입니다."),
    NOT_AUTHORIZED_TO_APPROVE(HttpStatus.UNAUTHORIZED,"P003","다른 미팅에서 보낸 승인이기 때문에 권한이 없습니다.");


    private HttpStatus status;
    private String code;
    private String message;
}
