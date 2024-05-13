package com.techeersalon.moitda.domain.user.controller;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserMeetingRecordRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @Operation(summary = "추가정보 입력")
    @PostMapping("/users")
    public ResponseEntity<String> signupUser(@RequestBody @Valid SignUpReq signUpReq) {

        userService.signup(signUpReq);

        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        userService.logout();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @Operation(summary = "회원정보 조회")
    @GetMapping("/users/{user_id}")
    public ResponseEntity<UserProfileRes> findUserProfile(@PathVariable("user_id") Long userId) {

        UserProfileRes userProfile = userService.findUserProfile(userId);

        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/users")
    public ResponseEntity<String> updateUserProfile(@RequestBody @Valid UpdateUserReq updateUserReq) {
        userService.updateUserProfile(updateUserReq);
        return ResponseEntity.ok("유저 정보 수정 완료");
    }
    @Operation(summary = "회원모임 내역 조회")
    @GetMapping("/users/{user_id}/records")
    public ResponseEntity<RecordsRes> getUserMeetingRecords(@PathVariable("user_id") Long userId) {
        RecordsRes meetingRecords = userService.getUserMeetingRecords(userId);
        return ResponseEntity.ok(meetingRecords);
    }
}