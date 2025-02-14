package kr.hailor.hailor.controller.reservation;

import kr.hailor.hailor.dto.ReservationDto;
import kr.hailor.hailor.entity.Reservation;
import kr.hailor.hailor.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // [필수] 예약 생성 엔드포인트 (대면/비대면 신청, 디자이너 선택, 날짜/시간 선택)
    @PostMapping
    public Reservation createReservation(
            @RequestBody ReservationDto dto,
            Authentication authentication
    ) {
        // JWT 인증된 사용자의 이메일을 통해 예약 생성
        String userEmail = authentication.getName();
        return reservationService.createReservation(userEmail, dto);
    }

    // 예약 목록 조회
    @GetMapping
    public List<Reservation> getMyReservations(Authentication authentication) {
        String userEmail = authentication.getName();
        return reservationService.getReservationsByUser(userEmail);
    }

    // 예약 상세 조회
    @GetMapping("/{id}/get_reservation")
    public Reservation getReservationDetail(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        return reservationService.getReservationDetail(id, userEmail);
    }

    // 예약 취소 (추후 구현)
    @DeleteMapping("/{id}")
    public String cancelReservation(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        reservationService.cancelReservation(id, userEmail);
        return "예약이 취소되었습니다. ID=" + id;
    }
}
