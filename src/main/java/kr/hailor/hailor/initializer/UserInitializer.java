package kr.hailor.hailor.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.hailor.hailor.entity.User;
import kr.hailor.hailor.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        String testEmail = "test@naver.com";
        String testUsername = "testuser";
        String testPassword = "test123"; // 실제 환경에서는 더 강력한 비밀번호 사용
        boolean testEnabled = true;
        String testVerificationToken = null; // 필요 시 설정

        // 이메일로 사용자를 찾습니다.
        if (!userRepository.findByEmail(testEmail).isPresent()) {
            User testUser = User.builder()
                    .email(testEmail)
                    .username(testUsername)
                    .password(passwordEncoder.encode(testPassword))
                    .enabled(testEnabled)
                    .verificationToken(testVerificationToken)
                    .build();
            userRepository.save(testUser);
            System.out.println("Test user created.");
        } else {
            System.out.println("Test user already exists.");
        }
    }
}
// 이거 테스트 계정 그냥 만들어 놓은거여서 나중에 패키지 자체를 삭제해도 됌.
