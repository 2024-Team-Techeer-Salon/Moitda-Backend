package com.techeersalon.moitda.domain.user.controller;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.TokenReq;
import com.techeersalon.moitda.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpReq signUpReq) {

        authService.SignUp(signUpReq);

        return ResponseEntity.ok("good");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid TokenReq tokenReq) {
        authService.Logout(tokenReq);
        return ResponseEntity.ok("logout");
    }
}