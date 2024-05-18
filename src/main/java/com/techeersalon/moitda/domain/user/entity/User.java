package com.techeersalon.moitda.domain.user.entity;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE user_id = ?")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "banner_image")
    private String bannerImage;

    @Column(name = "data_of_birth")
    private LocalDate dataOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "manner_stat", nullable = false)
    private Integer mannerStat;

    @Column(name = "location")
    private String location;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Column(name = "refresh_token")
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

    public void updateProfile(UpdateUserReq updateUserReq, String profileImage, String bannerImage) {
        this.username = updateUserReq.getUsername();
        this.profileImage = profileImage;
        this.bannerImage = bannerImage;
        this.gender = updateUserReq.getGender();
        this.introduce = updateUserReq.getIntroduce();
        this.location = updateUserReq.getLocation();
    }
}

