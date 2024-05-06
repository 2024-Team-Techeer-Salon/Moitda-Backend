package com.techeersalon.moitda.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.user.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpReq {
    @NotBlank(message = "이름 입력하세요")
    private String username;

    @NotBlank(message = "생년월일 입력하세요")
    private LocalDate dataOfBirth;

    @NotNull(message = "성별")
    private Gender gender;

    @NotBlank(message = "지역 입력하세요.")
    private String location;

}
