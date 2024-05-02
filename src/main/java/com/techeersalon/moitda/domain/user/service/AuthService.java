package com.techeersalon.moitda.domain.user.service;

import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.TokenReq;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    //나중에 리펙토링하기
    private final JwtService jwtService;

    public void SignUp(SignUpReq signUpReq) {
        //받아온 토큰을 활용하여 email과 socialtype을 가져와야 하나..?
        String accessToken = signUpReq.getAccessToken();

        Object[] result = jwtService.extractEmailAndSocialType(accessToken);
        if (result != null && result.length == 2) {
            String email = (String) result[0];
            SocialType socialType = (SocialType) result[1];

            Optional<User> optionalUser = userRepository.findBySocialTypeAndEmail(socialType, email);
            if (optionalUser.isPresent()) {
                // 이미 존재하는 사용자의 정보를 업데이트
                User existingUser = optionalUser.get();
                existingUser.SignUp(signUpReq);

                userRepository.save(existingUser);
            } else {
                //예외 처리 수정하기(사용자가 없습니다)
                throw new IllegalArgumentException("올바르지 않은 액세스 토큰입니다.");
            }
        } else {
            throw new IllegalArgumentException("올바르지 않은 액세스 토큰입니다.");
        }
    }

    public void Logout(TokenReq tokenReq) {

    }
}

