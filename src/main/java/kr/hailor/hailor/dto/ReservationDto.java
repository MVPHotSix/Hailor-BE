package kr.hailor.hailor.dto;

import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long designerId;                 // 선택한 디자이너 ID
    private String consultationType;         // "ONLINE" 또는 "OFFLINE"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDateTime; // 예약 날짜 및 시간
}
