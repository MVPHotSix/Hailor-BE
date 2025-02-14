package kr.hailor.hailor.dto.reservation;

import lombok.Data;

//예약 취소 요청 DTO
@Data
public class UserReservationsDeleteRequest {

  //사용자 고유 ID
  private Long userId;

  //취소할 예약 고유 ID
  private Long reservationId;
}