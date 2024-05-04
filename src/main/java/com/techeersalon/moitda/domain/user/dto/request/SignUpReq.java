package com.techeersalon.moitda.domain.user.dto.request;

import com.techeersalon.moitda.domain.user.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpReq {
    @NotBlank(message = "이름 입력하세요")
    private String username;

    @NotBlank(message = "생년월일 입력하세요")
    private String dataOfBirth;

    @NotNull(message = "성별")
    private Gender gender;

    @NotBlank(message = "지역 입력하세요.")
    private String location;

    @NotBlank(message = "엑세스 토큰이 없습니다.")
    private String accessToken;
}
