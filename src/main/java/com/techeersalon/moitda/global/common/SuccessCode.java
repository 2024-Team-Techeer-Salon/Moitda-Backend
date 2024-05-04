package com.techeersalon.moitda.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    //user
    USER_REGISTRATION_SUCCESS(HttpStatus.CREATED, "U001", "회원가입 성공"),

    //meeting
    MEETING_CREATE_SUCCESS(HttpStatus.CREATED,"M001", "프로젝트 생성 성공"),
    MEETING_GET_SUCCESS(HttpStatus.OK,"M002", "프로젝트 조회 성공");
    //MEETING_DELETE_SUCCESS(HttpStatus.CREATED,"M003", "프로젝트 삭제 성공"),
    //MEETING_UPDATE_SUCCESS(HttpStatus.CREATED,"M004", "프로젝트 수정 성공"),
    //MEETING_PAGING_GET_SUCCESS(HttpStatus.CREATED,"M005", "프로젝트 페이징 조회 성공");

    private HttpStatus status;
    private String code;
    private String message;
}
