package com.techeersalon.moitda.domain.user.service;

import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.UserIdRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.Gender;
import com.techeersalon.moitda.domain.user.entity.Role;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UserAlreadyRegisteredException;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static com.techeersalon.moitda.global.error.ErrorCode.USER_ALREADY_REGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    @Spy
    private UserService userService;
    @Spy
    private UserMapper userMapper;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("유저 추가입력")
    void signup() {

        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.GUEST)
                .mannerStat(40)
                .build();

        SignUpReq signUpReq = SignUpReq.builder()
                .username("Kang")
                .dataOfBirth(LocalDate.now())
                .gender(Gender.M)
                .location("고양시")
                .build();

        // 정의된 메서드가 호출되면 user 객체를 리턴하도록 모킹.
        doReturn(user).when(userService).getLoginUser();
        doReturn(user).when(userRepository).save(any());

        //when
        userService.signup(signUpReq);

        //then
        assertThat(user.getUsername()).isEqualTo(signUpReq.getUsername());
        assertThat(user.getDataOfBirth()).isEqualTo(signUpReq.getDataOfBirth());
        assertThat(user.getGender()).isEqualTo(signUpReq.getGender());
        assertThat(user.getLocation()).isEqualTo(signUpReq.getLocation());
    }

    @Test
    @DisplayName("이미 가입된 사용자")
    void alreadySignup() {

        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .build();

        SignUpReq signUpReq = SignUpReq.builder()
                .username("Kang")
                .dataOfBirth(LocalDate.now())
                .gender(Gender.M)
                .location("고양시")
                .build();

        doReturn(user).when(userService).getLoginUser();

        //when
        UserAlreadyRegisteredException exception = assertThrows(UserAlreadyRegisteredException.class,
                () -> userService.signup(signUpReq));

        //then
        assertThat(exception.getErrorCode().getCode()).isEqualTo(USER_ALREADY_REGISTERED.getCode());
        assertThat(exception.getErrorCode().getStatus()).isEqualTo(USER_ALREADY_REGISTERED.getStatus());
        assertThat(exception.getErrorCode().getMessage()).isEqualTo(USER_ALREADY_REGISTERED.getMessage());
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.GUEST)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();

        doReturn(user).when(userService).getLoginUser();
        doReturn(user).when(userRepository).save(any());

        //when
        userService.logout();

        //then
        assertThat(user.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("현재 사용자 조회")
    void findCurrentUserProfile() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.GUEST)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();

        doReturn(user).when(userService).getLoginUser();

        //when
        UserIdRes result = userService.findCurrentUserProfile();

        //then
        assertThat(result.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("현재 접속자가 다른 사용자 조회")
    void findUserProfile() {
        //given
        User loginUser = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();

        User foundUser = User.builder()
                .id(2L)
                .email("login@example.com")
                .role(Role.USER)
                .mannerStat(50)
                .build();

        doReturn(Optional.of(foundUser)).when(userRepository).findById(any());
        doReturn(loginUser).when(userService).getLoginUser();

        //when
        UserProfileRes result = userService.findUserProfile(foundUser.getId());

        //then
        assertThat(result.isOwner()).isFalse();
    }

    @Test
    @DisplayName("현재 접속자 자신을 조회")
    void findUserProfileMyself() {
        //given
        User loginUser = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();

        doReturn(Optional.of(loginUser)).when(userRepository).findById(any());
        doReturn(loginUser).when(userService).getLoginUser();

        //when
        UserProfileRes result = userService.findUserProfile(loginUser.getId());

        //then
        assertThat(result.isOwner()).isTrue();
    }

    @Test
    @DisplayName("넘어온 url = Null, 새로 s3에 업로드 될 데이터가 있을 경우.")
    void updateUserProfile() throws IOException {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();
        UpdateUserReq updateUserReq = UpdateUserReq.builder()
                .username("username")
                .gender(Gender.M)
                .introduce("wassup")
                .location("고양시 야옹야옹")
                .build();

        String profileUrl = null;
        String bannerUrl = null;
        MultipartFile profileImage = new MockMultipartFile(
                "profileImage", // field name
                "profileImage.jpg", // original file name
                "image/jpeg", // content type
                "profileImageContent".getBytes() // file content
        );

        MultipartFile bannerImage = new MockMultipartFile(
                "bannerImage", // field name
                "bannerImage.jpg", // original file name
                "image/jpeg", // content type
                "bannerImageContent".getBytes() // file content
        );

        doReturn(user).when(userService).getLoginUser();
        doReturn(user).when(userRepository).save(any());
        doReturn("newProfileImageUrl").when(userService).processImage(any(), eq("user/custom/profile/"));
        doReturn("newBannerImageUrl").when(userService).processImage(any(), eq("user/custom/banner/"));

        //when
        userService.updateUserProfile(updateUserReq, profileUrl, bannerUrl, profileImage, bannerImage);

        //then
        assertThat(user.getUsername()).isEqualTo(updateUserReq.getUsername());
        assertThat(user.getIntroduce()).isEqualTo(updateUserReq.getIntroduce());
        assertThat(user.getLocation()).isEqualTo(updateUserReq.getLocation());
        assertThat(user.getProfileImage()).isEqualTo("newProfileImageUrl");
        assertThat(user.getBannerImage()).isEqualTo("newBannerImageUrl");
    }
    @Test
    @DisplayName("넘어온 url = Null, 새로 업로드 될 데이터가 없을 경우.")
    void updateUserProfile_withNullUrlsAndNoUploads() throws IOException {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .build();
        UpdateUserReq updateUserReq = UpdateUserReq.builder()
                .username("username")
                .gender(Gender.M)
                .introduce("wassup")
                .location("고양시 야옹야옹")
                .build();

        String profileUrl = null;
        String bannerUrl = null;

        doReturn(user).when(userService).getLoginUser();
        doReturn(user).when(userRepository).save(any());

        //when
        userService.updateUserProfile(updateUserReq, profileUrl, bannerUrl, null, null);

        //then
        assertThat(user.getUsername()).isEqualTo(updateUserReq.getUsername());
        assertThat(user.getIntroduce()).isEqualTo(updateUserReq.getIntroduce());
        assertThat(user.getLocation()).isEqualTo(updateUserReq.getLocation());
        assertThat(user.getProfileImage()).isNull();
        assertThat(user.getBannerImage()).isNull();
    }

    @Test
    @DisplayName("넘어온 url이 존재하고 새로 업로드 될 데이터가 없는 경우.")
    void updateUserProfile_withExistingUrlsAndNoUploads() throws IOException {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .role(Role.USER)
                .mannerStat(40)
                .refreshToken("refreshToken")
                .profileImage("existingProfileImageUrl")
                .bannerImage("existingBannerImageUrl")
                .build();
        UpdateUserReq updateUserReq = UpdateUserReq.builder()
                .username("username")
                .gender(Gender.M)
                .introduce("wassup")
                .location("고양시 야옹야옹")
                .build();

        String profileUrl = "existingProfileImageUrl";
        String bannerUrl = "existingBannerImageUrl";

        doReturn(user).when(userService).getLoginUser();
        doReturn(user).when(userRepository).save(any());

        //when
        userService.updateUserProfile(updateUserReq, profileUrl, bannerUrl, null, null);

        //then
        assertThat(user.getUsername()).isEqualTo(updateUserReq.getUsername());
        assertThat(user.getIntroduce()).isEqualTo(updateUserReq.getIntroduce());
        assertThat(user.getLocation()).isEqualTo(updateUserReq.getLocation());
        assertThat(user.getProfileImage()).isEqualTo("existingProfileImageUrl");
        assertThat(user.getBannerImage()).isEqualTo("existingBannerImageUrl");
    }
}