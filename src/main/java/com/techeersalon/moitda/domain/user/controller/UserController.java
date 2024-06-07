package com.techeersalon.moitda.domain.user.controller;

import com.techeersalon.moitda.domain.meetings.dto.response.GetSearchPageRes;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.request.UserTokenReq;
import com.techeersalon.moitda.domain.user.dto.response.UserIdRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.service.UserService;
import com.techeersalon.moitda.global.common.SuccessResponse;
import com.techeersalon.moitda.global.jwt.JwtToken;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.techeersalon.moitda.global.common.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final MeetingService meetingService;
    private final JwtService jwtService;

    @Operation(summary = "추가정보 입력")
    @PostMapping("/users")
    public ResponseEntity<SuccessResponse> signupUser(@RequestBody @Valid SignUpReq signUpReq) {
        userService.signup(signUpReq);
        return ResponseEntity.ok(SuccessResponse.of(USER_REGISTRATION_SUCCESS));

    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logoutUser() {

        userService.logout();

        return ResponseEntity.ok(SuccessResponse.of(USER_LOGOUT_SUCCESS));
    }

    @Operation(summary = "현재 사용자 정보 조회")
    @GetMapping("/users/me")
    public ResponseEntity<SuccessResponse> findCurrentUserProfile() {

        UserIdRes userProfile = userService.findCurrentUserProfile();

        return ResponseEntity.ok(SuccessResponse.of(USER_PROFILE_GET_SUCCESS, userProfile));
    }

    @Operation(summary = "회원정보 조회")
    @GetMapping("/users/{user_id}")
    public ResponseEntity<SuccessResponse> findUserProfile(@PathVariable("user_id") Long userId) {

        UserProfileRes userProfile = userService.findUserProfile(userId);

        return ResponseEntity.ok(SuccessResponse.of(USER_PROFILE_GET_SUCCESS, userProfile));
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> updateUserProfile(
            @RequestPart @Valid UpdateUserReq updateUserReq,
            @RequestPart(required = false) @Valid String profileUrl,
            @RequestPart(required = false) @Valid String bannerUrl,
            @RequestPart(name = "profile_image_file", required = false) @Valid MultipartFile profileImageFile,
            @RequestPart(name = "banner_image_file", required = false) @Valid MultipartFile bannerImageFile
    ) throws IOException {

        userService.updateUserProfile(updateUserReq, profileUrl, bannerUrl, profileImageFile, bannerImageFile);

        return ResponseEntity.ok(SuccessResponse.of(USER_PROFILE_UPDATE_SUCCESS));
    }

    @Operation(summary = "회원모임 생성내역 조회")
    @GetMapping("/users/{user_id}/records/created")
    public ResponseEntity<SuccessResponse> getUserMeetingCreatingRecords(@PathVariable("user_id") Long userId,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createAt")));
        GetSearchPageRes meetingRecords = meetingService.getUserMeetingCreatingRecords(userId, pageable);
        return ResponseEntity.ok(SuccessResponse.of(USER_MEETING_RECORD_GET_SUCCESS, meetingRecords));
    }

    @Operation(summary = "회원모임 참여내역 조회")
    @GetMapping("/users/{user_id}/records/participated")
    public ResponseEntity<SuccessResponse> getUserMeetingParticipationRecords(@PathVariable("user_id") Long userId,
                                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createAt")));
        GetSearchPageRes meetingRecords = meetingService.getUserMeetingParticipationRecords(userId, pageable);
        return ResponseEntity.ok(SuccessResponse.of(USER_MEETING_RECORD_GET_SUCCESS, meetingRecords));
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse> reGenerateAccessToken(@RequestBody @Valid UserTokenReq userTokenReq) {

        JwtToken newToken = userService.reissueToken(userTokenReq);
        return ResponseEntity.ok(SuccessResponse.of(TOKEN_CREATE_SUCCESS, newToken));
    }
}