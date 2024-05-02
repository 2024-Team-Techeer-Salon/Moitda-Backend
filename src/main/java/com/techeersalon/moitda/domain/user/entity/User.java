package com.techeersalon.moitda.domain.user.entity;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
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

//    @Column(length = 5, nullable = false)
    private String username;

    // 길이 설정을 나중에 제대로 수정해야 하지 않을까요..?
    @Column(nullable = false)
    private String email;

    //image 모두 true로 해도 됳 듯?
//    @Column(nullable = false)
    private String profileImage;

    //    @Column(nullable = false)
    private String bannerImage;

//    private LocalDate dataOfBirth;
    private String dataOfBirth;

//    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String introduction;

    @Column(nullable = false)
    private Float mannersTemperature;

//    @Column(nullable = false)
    private String location;

    //초기 사용자인지 아닌지 여부
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String refreshToken;
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)


    @Builder
    private User(String username, String email, String profileImage, String bannerImage, String dataOfBirth, Gender gender, String introduction, Float mannersTemperature, String location, Role role, SocialType socialType, String refreshToken) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.bannerImage = bannerImage;
        this.dataOfBirth = dataOfBirth;
        this.gender = gender;
        this.introduction = introduction;
        this.mannersTemperature = mannersTemperature;
        this.location = location;
        this.role = role;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void SignUp(SignUpReq signUpReq) {
        this.username = signUpReq.getUsername();
        this.dataOfBirth = signUpReq.getDataOfBirth();
        this.gender = signUpReq.getGender();
        this.location = signUpReq.getLocation();
        this.role = Role.USER;
    }
}

