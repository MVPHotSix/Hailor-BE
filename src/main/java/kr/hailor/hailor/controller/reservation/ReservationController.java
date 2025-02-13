package kr.hailor.hailor.controller.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hailor.hailor.dto.ReservationResponseDto;
import kr.hailor.hailor.entity.Reservation;
import kr.hailor.hailor.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "예약 관리 API")
public class ReservationController {

  private final ReservationService reservationService;

  @Operation(summary = "예약 목록 조회", description = "사용자의 예약 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping
  public ResponseEntity<List<ReservationResponseDto>> getMyReservations(
          Authentication authentication) {
    String userEmail = authentication.getName();
    List<ReservationResponseDto> reservations =
            reservationService.getReservationsByUser(userEmail);
    return ResponseEntity.ok(reservations);
  }

  @Operation(summary = "예약 상세 조회", description = "예약 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> getReservationDetail(
          @Parameter(description = "예약 ID") @PathVariable Long id,
          Authentication authentication) {
    String userEmail = authentication.getName();
    ReservationResponseDto reservation =
            reservationService.getReservationDetail(id, userEmail);
    return ResponseEntity.ok(reservation);
  }

  @Operation(summary = "예약 취소", description = "예약을 취소합니다.")
  @ApiResponse(responseCode = "200", description = "취소 성공")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> cancelReservation(
          @Parameter(description = "예약 ID") @PathVariable Long id,
          Authentication authentication) {
    String userEmail = authentication.getName();
    reservationService.cancelReservation(id, userEmail);
    return ResponseEntity.ok("예약이 취소되었습니다. ID=" + id);
  }
}