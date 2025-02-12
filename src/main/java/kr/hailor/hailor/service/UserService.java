package kr.hailor.hailor.service;

import kr.hailor.hailor.dto.RegisterResponseDto;
import kr.hailor.hailor.dto.UserDto;
import kr.hailor.hailor.entity.User;
import kr.hailor.hailor.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private kr.hailor.hailor.service.EmailService emailService;

    // 회원가입 로직
    public RegisterResponseDto registerUser(UserDto userDto) {
        // 1) 중복 이메일 체크
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return new RegisterResponseDto(false, "이미 존재하는 이메일입니다.");
        }

        // 2) 인증 토큰 생성
        String token = UUID.randomUUID().toString();

        // 3) 이메일 발송 (인증 링크 포함)
        emailService.sendEmail(userDto.getEmail(), "Email Verification",
                "<h1 style='font-family: Arial, sans-serif; color: #333;'>이메일 인증</h1>" +
                        "<p style='font-family: Arial, sans-serif; color: #555;'>아래 버튼을 클릭하여 이메일 인증을 완료하세요:</p>" +
                        "<a href='http://localhost:8080/api/users/verify?token=" + token + "' " +
                        "style='display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; " +
                        "text-decoration: none; border-radius: 5px; font-family: Arial, sans-serif; font-size: 16px;'>이메일 인증</a>" +
                        "<hr style='border: none; border-top: 1px solid #ccc; margin: 20px 0;'>" +
                        "<p style='font-family: Arial, sans-serif; color: #555;'>만약 버튼이 보이지 않는다면, 아래 링크를 복사해서 브라우저에 붙여넣으세요:</p>" +
                        "<p style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 10px; border: 1px solid #ddd; " +
                        "border-radius: 5px; word-break: break-word;'>http://localhost:8080/api/users/verify?token=" + token + "</p>");

        // 4) DB 저장
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername() != null ? userDto.getUsername() : "기본 사용자") // username 설정
                .enabled(false) // 인증 전
                .verificationToken(token)
                .build();

        userRepository.save(user);

        // 5) 반환
        return new RegisterResponseDto(true, "회원가입이 완료되었습니다. 이메일을 확인하세요.");
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            user.setVerificationToken(null);
            return true;
        }
        return false;
    }
}
