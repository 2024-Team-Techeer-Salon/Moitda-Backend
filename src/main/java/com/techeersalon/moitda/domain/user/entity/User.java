package com.techeersalon.moitda.domain.user.entity;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Column(nullable = false)
    private String email;

    private String profileImage;

    private String bannerImage;

    private LocalDate dataOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String introduce;

    @Column(nullable = false)
    private Integer mannerStat;

    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String refreshToken;

    @Builder
    private User(String username, String email, String profileImage, String bannerImage, LocalDate dataOfBirth, Gender gender, String introduce, Integer mannerStat, String location, Role role, SocialType socialType, String refreshToken) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.bannerImage = bannerImage;
        this.dataOfBirth = dataOfBirth;
        this.gender = gender;
        this.introduce = introduce;
        this.mannerStat = mannerStat;
        this.location = location;
        this.role = role;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void signupUser(SignUpReq signUpReq) {
        this.username = signUpReq.getUsername();
        this.dataOfBirth = signUpReq.getDataOfBirth();
        this.gender = signUpReq.getGender();
        this.location = signUpReq.getLocation();
        this.role = Role.USER;
    }

    public void onLogout() {
        this.refreshToken = null;
    }

    public void updateProfile(UpdateUserReq updateUserReq) {
        this.username = updateUserReq.getUsername();
        this.profileImage = updateUserReq.getProfileImage();
        this.bannerImage = updateUserReq.getBannerImage();
        this.gender = updateUserReq.getGender();
        this.introduce = updateUserReq.getIntroduce();
        this.location = updateUserReq.getLocation();
    }
}

