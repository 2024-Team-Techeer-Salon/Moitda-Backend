package com.techeersalon.moitda.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    //user
    USER_REGISTRATION_SUCCESS(HttpStatus.CREATED, "U001", "회원가입 성공"),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "U002", "로그아웃 성공"),
    USER_PROFILE_GET_SUCCESS(HttpStatus.OK, "U003", "회원정보 조회 성공"),
    USER_PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "U004", "회원정보 수정 성공"),
    USER_MEETING_RECORD_GET_SUCCESS(HttpStatus.OK, "U005", "회원 모임 기록 조회 성공"),

    //meeting
    MEETING_CREATE_SUCCESS(HttpStatus.CREATED, "M001", "모임 생성 성공"),
    MEETING_GET_SUCCESS(HttpStatus.OK, "M002", "모임 조회 성공"),
    MEETING_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "M003", "모임 삭제 성공"),
    MEETING_PAGING_GET_SUCCESS(HttpStatus.OK, "M004", "모임 페이징 조회 성공"),
    MEETING_UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "M005", "모임 수정 성공"),
    MEETING_END_SUCCESS(HttpStatus.NO_CONTENT, "M006", "모임 종료 성공"),
    MEETING_SEARCH_SUCCESS(HttpStatus.OK, "M007", "모임 검색 성공"),

    //participant
    PARTICIPANT_CREATE_SUCCESS(HttpStatus.CREATED, "P001", "참가자 생성 성공"),
    PARTICIPANT_APPROVAL_OR_REJECTION_SUCCESS(HttpStatus.NO_CONTENT, "P002", "참가자 승인 또는 거절 성공"),
    PARTICIPANT_LIST_GET_SUCCESS(HttpStatus.OK, "P003", "모임 신청자 목록 조회"),

    //review
    REVIEW_CREATE_SUCCESS(HttpStatus.CREATED, "R001", "후기 생성 성공"),

    ;
    private HttpStatus status;
    private String code;
    private String message;
}
