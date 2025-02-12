package kr.hailor.hailor.repository;

import kr.hailor.hailor.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 특정 사용자의 예약 목록 조회 (예약 조회 기능은 추후 개발 예정)
    List<Reservation> findByUserId(Long userId);
}
