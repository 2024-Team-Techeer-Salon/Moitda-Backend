package com.techeersalon.moitda.domain.user.dto.mapper;

import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileRes toUserProfile(User user) {
        return UserProfileRes.builder()
                .username(user.getUsername())
                .mannerStat(user.getMannerStat())
                .profileImage(user.getProfileImage())
                .bannerImage(user.getBannerImage())
                .gender(user.getGender())
                .introduce(user.getIntroduce())
                .location(user.getLocation())
                .build();
    }
}
