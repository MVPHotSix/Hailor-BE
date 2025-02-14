package kr.hailor.hailor.repository;

import kr.hailor.hailor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 찾기
    Optional<User> findByEmail(String email);

    // 인증 토큰으로 찾기 (이메일 인증용)
    Optional<User> findByVerificationToken(String token);
}
