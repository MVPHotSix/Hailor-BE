package kr.hailor.hailor.service;


import kr.hailor.hailor.dto.ReservationResponseDto;
import kr.hailor.hailor.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;

  public List<ReservationResponseDto> getReservationsByUser(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

    return reservationRepository.findByUserOrderByReservationTimeDesc(user)
            .stream()
            .map(ReservationResponseDto::new)
            .collect(Collectors.toList());
  }

  public ReservationResponseDto getReservationDetail(Long id, String userEmail) {
    Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ReservationNotFoundException("예약을 찾을 수 없습니다."));

    validateReservationOwner(reservation, userEmail);

    return new ReservationResponseDto(reservation);
  }

  @Transactional
  public void cancelReservation(Long id, String userEmail) {
    Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ReservationNotFoundException("예약을 찾을 수 없습니다."));

    validateReservationOwner(reservation, userEmail);
    validateCancellation(reservation);

    reservation.cancel();
  }

  private void validateReservationOwner(Reservation reservation, String userEmail) {
    if (!reservation.getUser().getEmail().equals(userEmail)) {
      throw new UnauthorizedException("해당 예약에 대한 권한이 없습니다.");
    }
  }

  private void validateCancellation(Reservation reservation) {
    LocalDateTime now = LocalDateTime.now();
    if (reservation.getReservationTime().isBefore(now)) {
      throw new InvalidReservationStateException("지난 예약은 취소할 수 없습니다.");
    }
    if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
      throw new InvalidReservationStateException("확정된 예약만 취소할 수 있습니다.");
    }
  }
}