package kr.hailor.hailor.config;

import kr.hailor.hailor.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// OAuth2(구글) 로그인 성공 후, JWT 발급 및 리다이렉트 처리

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1) 여기서 'authentication'에는 구글에서 받은 사용자 정보가 들어있음
        //    (CustomOAuth2UserService에서 loadUser 한 정보가 세팅됨)

        // 2) JWT 발급
        String jwtToken = jwtTokenProvider.generateToken(authentication);

        // 3) 클라이언트(React 등)로 토큰 전달 (예: 리다이렉트 + 쿼리 파라미터) 나중에 완성되면 정확한 주소로 연결
        response.sendRedirect("http://localhost:8080/??" + jwtToken);
    }
}
