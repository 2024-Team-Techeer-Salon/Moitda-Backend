package com.techeersalon.moitda.global.jwt.Service;

import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String SOCIAL_TYPE_CLAIM = "socialType";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email, SocialType socialType) {
        Date now = new Date();
        return Jwts.builder() // JWT 토큰을 생성하는 빌더 반환
                .setSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
                .claim(EMAIL_CLAIM, email)
                .claim(SOCIAL_TYPE_CLAIM, socialType)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public void updateRefreshToken(String email, SocialType socialType, String refreshToken) {
        userRepository.findBySocialTypeAndEmail(socialType, email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public Object[] extractEmailAndSocialType(String accessToken) {
        try {
            Claims claims = decodeAccessToken(accessToken);
            if (claims != null) {
                String email = claims.get("email", String.class);
                SocialType socialType = deserializeSocialType(claims.get("socialType", String.class));
                return new Object[]{email, socialType};
            }
        } catch (Exception e) {
            // 디코딩 실패 시 예외 처리
            e.printStackTrace();
        }
        return null;
    }
    // Claims에 저장할 때 SocialType을 문자열로 변환
    private String serializeSocialType(SocialType socialType) {
        // 예시: SocialType이 Enum일 경우 name() 메서드를 사용하여 문자열로 변환
        return socialType.name();
    }
    // Claims에서 추출할 때 문자열을 SocialType으로 변환
    private SocialType deserializeSocialType(String socialTypeString) {
        // 예시: 문자열을 Enum으로 변환
        return SocialType.valueOf(socialTypeString);
    }

    private Claims decodeAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            // 디코딩 실패 시 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}