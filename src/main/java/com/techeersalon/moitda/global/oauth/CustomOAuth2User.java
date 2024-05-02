package com.techeersalon.moitda.global.oauth;

import com.techeersalon.moitda.domain.user.entity.Role;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가진다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private Role role;
    private SocialType socialType;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role, SocialType socialType) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
        this.socialType = socialType;
    }
}
