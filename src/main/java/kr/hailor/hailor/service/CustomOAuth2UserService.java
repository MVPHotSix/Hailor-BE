package kr.hailor.hailor.service;

import kr.hailor.hailor.entity.User;
import kr.hailor.hailor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * Google OAuth2 로그인 시, 사용자 정보를 받아 DB에 저장하거나 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // DB 접근용

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1) 구글에서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2) 디버깅용 로그
        System.out.println("구글 사용자 정보: " + oAuth2User.getAttributes());

        // 3) attributes에서 email, name 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name  = (String) attributes.get("name");  // profile 범위를 허용하면 가져올 수 있음

        if (email == null) {
            throw new IllegalArgumentException("구글 응답에서 이메일을 찾을 수 없습니다.");
        }

        // 4) DB에서 해당 이메일 사용자가 없으면 자동 회원가입
        User user = userRepository.findByEmail(email)
                .orElseGet(() ->
                        userRepository.save(
                                User.builder()
                                        .email(email)
                                        .username(name != null ? name : "구글사용자")
                                        .password("") // 소셜 로그인은 별도 비번 필요 없으니 빈 값
                                        .enabled(true) // 별도 이메일 인증 없이 활성화
                                        .build()
                        )
                );

        // 5) DefaultOAuth2User 생성 (ROLE_USER 부여)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                // user-name-attribute-name
                // 기본적으로 구글의 "sub"이 되지만, 여기서는 email을 사용하려면 "email"로 지정
                "email"
        );
    }
}
