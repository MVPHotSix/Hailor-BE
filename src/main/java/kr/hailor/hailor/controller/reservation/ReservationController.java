package kr.hailor.hailor.controller.reservation;

import kr.hailor.hailor.dto.ReservationDto;
import kr.hailor.hailor.dto.ReservationResponseDto;
import kr.hailor.hailor.entity.Reservation;
import kr.hailor.hailor.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성 엔드포인트
    @PostMapping
    public ReservationResponseDto createReservation(
            @RequestBody ReservationDto dto,
            Authentication authentication
    ) {
        // TestAuthenticationFilter가 주입한 "test@example.com"이 사용됩니다.
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
    @GetMapping("/{id}")
    public Reservation getReservationDetail(
            @PathVariable("id") Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        return reservationService.getReservationDetail(id, userEmail);
    }

    // 예약 취소
    @DeleteMapping("/{id}")
    public String cancelReservation(
            @PathVariable("id") Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        reservationService.cancelReservation(id, userEmail);
        return "예약이 취소되었습니다. ID=" + id;
    }

    // 특정 날짜에 대한 예약 가능한 시간 슬롯 조회
    @GetMapping("/available-slots")
    public List<LocalTime> getAvailableSlots(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDate today = LocalDate.now();
        if (date.isBefore(today) || date.isAfter(today.plusMonths(3))) {
            throw new RuntimeException("예약 가능 날짜는 오늘부터 향후 3개월까지 가능합니다.");
        }

        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(20, 0);
        while (!start.isAfter(end)) {
            slots.add(start);
            start = start.plusMinutes(30);
        }
        return slots;
    }
}
