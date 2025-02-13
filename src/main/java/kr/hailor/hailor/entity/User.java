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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // 이메일 인증 여부
    @Column(nullable = false)
    private boolean enabled;

    // 이메일 인증 토큰
    @Column
    private String verificationToken;

    // 이메일 인증 여부 설정
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // 인증 토큰 설정
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

}