package kr.hailor.hailor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponseDto {
    private boolean success; // 가입 성공 여부
    private String message;  // 상세 메시지
}
