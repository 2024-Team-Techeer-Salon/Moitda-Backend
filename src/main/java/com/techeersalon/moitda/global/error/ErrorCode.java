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
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "U002", "토큰이 유효하지 않습니다."),
    USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "U003", "이미 회원가입된 사용자입니다."),
    // 채팅방 접근 시
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "U004", "가입된 사용자가 아닙니다"),

    // 모임
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "미팅을 찾을 수 없습니다."),
    MEETING_MAX_PARTICIPANTS_NOT_EXCEEDED(HttpStatus.BAD_REQUEST, "M002", "최대 참가 인원 수가 너무 적습니다."),
    MEETING_PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M003", "미팅 페이지를 찾을 수 없습니다."),
    MEETING_IS_FULL(HttpStatus.BAD_REQUEST, "M004", "참여자가 가득 찼습니다."),
    MEETING_NOT_ENDED(HttpStatus.BAD_REQUEST, "M005", "미팅이 아직 종료되지 않았습니다."),
    MEETING_OWNER_LEAVING_EXCEPTION(HttpStatus.BAD_REQUEST, "M006", "방장은 모임에서 나갈 수 없습니다."),

    // 참여자
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "참여자을 찾을 수 없습니다."),
    ALREADY_PARTICIPATING_OR_APPLIED(HttpStatus.NOT_FOUND, "P002", "이미 모임에 신청한 상태 거나 참가자 입니다."),
    NOT_AUTHORIZED_TO_APPROVE(HttpStatus.UNAUTHORIZED, "P003", "다른 미팅에서 보낸 승인이기 때문에 권한이 없습니다."),

    // 채팅방
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"CR001","채팅방을 찾을 수 없습니다."),
    ROOM_OWNER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "CR002", "방장은 채팅방에서 탈퇴할 수 없습니다."),
    ALREADY_PARTICIPATING(HttpStatus.BAD_REQUEST, "CR003", "이미 채팅방에 참여한 상태입니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"CM001","메시지를 찾을 수 없습니다"),

    // 평가
    INVALID_RATING_SCORE(HttpStatus.BAD_REQUEST, "R001", "평가 점수는 1부터 5까지만 가능합니다."),
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "R002", "이미 리뷰를 마쳤습니다.")

    ;

    private HttpStatus status;
    private String code;
    private String message;
}
