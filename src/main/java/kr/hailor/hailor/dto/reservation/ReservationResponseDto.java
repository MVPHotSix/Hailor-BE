package kr.hailor.hailor.dto.reservation;

import kr.hailor.hailor.entity.Reservation;
import kr.hailor.hailor.entity.ReservationStatus;
import lombok.Getter;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationResponseDto {
  private Long id;                          // 예약 ID
  private String designerName;              // 디자이너 이름
  private LocalDateTime reservationTime;    // 예약 시간
  private ReservationStatus status;         // 예약 상태
  private String consultationType;          // 상담 유형
  private String userName;                  // 예약자 이름
  private LocalDateTime createdAt;          // 예약 생성 시간

  public static ReservationResponseDto from(Reservation reservation) {
    return ReservationResponseDto.builder()
            .id(reservation.getId())
            .designerName(reservation.getDesigner().getName())
            .reservationTime(reservation.getReservationDateTime())
            .status(reservation.getStatus())
            .consultationType(reservation.getType().name())
            .userName(reservation.getUser().getUsername())
            .createdAt(reservation.getCreatedAt())
            .build();
  }
}