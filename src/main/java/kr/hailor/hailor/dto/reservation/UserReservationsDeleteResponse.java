package kr.hailor.hailor.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//예약 취소 응답 DTO
@Data
@Builder
@AllArgsConstructor
public class UserReservationsDeleteResponse {

  //취소 성공 여부
  private boolean success;

  //취소 결과 메시지
  private String message;
}
