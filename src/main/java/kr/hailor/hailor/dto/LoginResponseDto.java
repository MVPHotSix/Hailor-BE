package kr.hailor.hailor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;      // JWT
    private String tokenType;  // "Bearer"
}
