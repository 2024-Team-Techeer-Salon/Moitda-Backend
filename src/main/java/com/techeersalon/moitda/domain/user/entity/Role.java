package com.techeersalon.moitda.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/*
key 필드를 추가하여 "ROLE_"을 붙인 이유는, 스프링 시큐리티에서는 권한(Role) 코드에
항상 "ROLE_" 접두사가 앞에 붙어야 하기 떄문에 "ROLE_"을 설정
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER");

    private final String key;
}
