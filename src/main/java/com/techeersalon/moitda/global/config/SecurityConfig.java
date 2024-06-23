package com.techeersalon.moitda.global.config;

import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.jwt.JwtAuthenticationFilter;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
import com.techeersalon.moitda.global.jwt.exception.JwtAccessDeniedHandler;
import com.techeersalon.moitda.global.jwt.exception.JwtAuthenticationEntryPoint;
import com.techeersalon.moitda.global.oauth.OAuth2LoginFailureHandler;
import com.techeersalon.moitda.global.oauth.OAuth2LoginSuccessHandler;
import com.techeersalon.moitda.global.oauth.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity //시큐리티 활성화 -> 기본 스프링 필터 체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:9100",
                "http://localhost:9090",
                "http://jxy.me",
                "http://apic.app",
                "file://",
                "https://jiangxy.github.io",
                "http://localhost",
                "https://moitda.site",
                "http://moitda.site"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/oauth2/**",
                                "/index.html",
                                "/ws/**",
                                "/swagger/**",
                                "swagger-ui/**",
                                "/api-docs/**",
                                "/signup.html",
                                "/api/v1/reissue"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler) // 2.
                        .failureHandler(oAuth2LoginFailureHandler) // 3.
                )
                .exceptionHandling(authenticationManager ->authenticationManager
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
