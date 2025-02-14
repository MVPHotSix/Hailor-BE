package kr.hailor.hailor.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;    // 필수
    private String password; // 필수
    private String username; // 선택 (기존 nickname)
}
