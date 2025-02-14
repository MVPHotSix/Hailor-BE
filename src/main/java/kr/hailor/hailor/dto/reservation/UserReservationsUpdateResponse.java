package kr.hailor.hailor.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//예약 수정 응답 DTO
@Data
@Builder
@AllArgsConstructor
public class UserReservationsUpdateResponse {

  //수정 성공 여부
  private boolean success;

  //수정 결과 메시지
  private String message;
}