package kr.hailor.hailor.dto.reservation;

import lombok.Data;

// 예약 수정 요청 DTO
// 예: 예약 일시 변경 등
@Data
public class UserReservationsUpdateRequest {

  //사용자 고유 ID
  private Long userId;

  //수정할 예약 고유 ID
  private Long reservationId;

  //새로 변경할 예약 일시 (예: "2025-02-16T14:08:00" 형식)
  private String newReservationTime;
}