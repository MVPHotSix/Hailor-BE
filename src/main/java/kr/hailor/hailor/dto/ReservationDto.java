package kr.hailor.hailor.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long designerId;                 // 선택한 디자이너 ID
    private String consultationType;         // "ONLINE" 또는 "OFFLINE"
    private LocalDateTime reservationDateTime; // 예약 날짜 및 시간
}
