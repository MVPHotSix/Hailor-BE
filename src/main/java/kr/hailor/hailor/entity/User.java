package kr.hailor.hailor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일을 유일 식별자로 사용 (로그인 ID)
    @Column(nullable = false, unique = true)
    private String email;

    // 인코딩된 비밀번호
    @Column(nullable = false)
    private String password;

    // 이메일 인증 여부
    @Column(nullable = false)
    private boolean enabled;

    // 이메일 인증 토큰(선택)
    private String verificationToken;

    // 사용자 이름: 기존 nickname을 username으로 변경
    @Column(nullable = false)
    private String username;

    // 이메일 인증 여부 설정
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // 인증 토큰 설정
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

}
