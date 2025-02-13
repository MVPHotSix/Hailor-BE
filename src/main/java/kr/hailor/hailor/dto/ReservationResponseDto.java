package kr.hailor.hailor.dto;

import kr.hailor.hailor.entity.ConsultationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private String designerName;
    private LocalDateTime reservationDateTime;
    private ConsultationType consultationType;
    private int price;
    private String paymentAccount; // 결제 계좌 정보
}

