package com.techeersalon.moitda.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.techeersalon.moitda.domain.user.entity.Gender;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileRes {

    private String username;
    private int mannerStat;
    private String profileImage;
    private String bannerImage;
    private Gender gender;
    private String introduce;
    private String location;

}
