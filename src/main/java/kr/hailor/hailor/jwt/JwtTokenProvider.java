package kr.hailor.hailor.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final kr.hailor.hailor.jwt.JwtProperties jwtProperties;

    // 예: 1시간
    private final long validityInMilliseconds = 3600000L;

    @PostConstruct
    public void debugSecretKeyLength() {
        String raw = jwtProperties.getSecretKey(); // .env에서 로드된 문자열
        System.out.println(">>> [DEBUG] RAW secretKey = [" + raw + "]");
        System.out.println(">>> [DEBUG] raw.length() = " + raw.length());

        byte[] bytes = raw.getBytes();
        System.out.println(">>> [DEBUG] raw.getBytes().length = " + bytes.length + " (in bits: " + (bytes.length*8) + ")");
    }

    public String generateToken(Authentication authentication) {
        String base64Key = jwtProperties.getSecretKey();
        // "4OTz8XFkWv4i3N9K..."

        // ① Base64 decode
        byte[] decoded = io.jsonwebtoken.io.Decoders.BASE64.decode(base64Key);

        // ② decode된 raw 바이트를 hmacShaKeyFor에 전달
        Key key = Keys.hmacShaKeyFor(decoded);

        // ③ 토큰 생성
        String email = authentication.getName();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getEmail(String token) {
        // 파싱해 subject = email 꺼내기
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        // Base64 decode 먼저
        byte[] decoded = io.jsonwebtoken.io.Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Jwts.parserBuilder()
                .setSigningKey(decoded)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
