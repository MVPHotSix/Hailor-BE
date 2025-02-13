package kr.hailor.hailor.service;

import kr.hailor.hailor.dto.ReservationDto;
import kr.hailor.hailor.dto.ReservationResponseDto;
import kr.hailor.hailor.entity.*;
import kr.hailor.hailor.repository.DesignerRepository;
import kr.hailor.hailor.repository.ReservationRepository;
import kr.hailor.hailor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;

    // [필수] 예약 생성: 대면/비대면 옵션, 디자이너 선택, 예약 날짜/시간을 받아 예약을 생성함
    public ReservationResponseDto createReservation(String userEmail, ReservationDto dto) {
        // 1. 예약 신청한 사용자(회원) 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다: " + userEmail));

        // 2. 선택한 디자이너 조회
        Designer designer = designerRepository.findById(dto.getDesignerId())
                .orElseThrow(() -> new RuntimeException("디자이너를 찾을 수 없습니다: ID=" + dto.getDesignerId()));

        // 3. 대면/비대면 옵션 파싱
        ConsultationType type;
        try {
            type = ConsultationType.valueOf(dto.getConsultationType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 컨설팅 타입: " + dto.getConsultationType());
        }

        // 4. 예약 시간/날짜 검증
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = dto.getReservationDateTime();
        if (reservationDateTime.isBefore(now)) {
            throw new RuntimeException("예약 시간은 현재보다 미래여야 합니다.");
        }
        // 예약 가능 기간: 현재부터 3개월 후까지
        if (reservationDateTime.isAfter(now.plusMonths(3))) {
            throw new RuntimeException("예약 가능 날짜는 최대 향후 3개월까지 가능합니다.");
        }
        // 예약 가능한 시간: 오전 10시 ~ 오후 20시 (오후 20시는 정확히 20:00만 허용)
        LocalTime time = reservationDateTime.toLocalTime();
        if (time.isBefore(LocalTime.of(10, 0)) || time.isAfter(LocalTime.of(20, 0))) {
            throw new RuntimeException("예약 시간은 오전 10시부터 오후 20시 사이여야 합니다.");
        }
        if (time.getHour() == 20 && time.getMinute() != 0) {
            throw new RuntimeException("오후 20시 이후 시간은 예약할 수 없습니다.");
        }
        // 30분 단위 검증: 분이 0 또는 30이어야 함
        if (time.getMinute() != 0 && time.getMinute() != 30) {
            throw new RuntimeException("예약 시간은 30분 단위로 선택해야 합니다.");
        }

        // 5. 예약 금액 결정 (디자이너의 offlinePrice 또는 onlinePrice 선택)
        int price = (type == ConsultationType.OFFLINE)
                ? designer.getOfflinePrice()
                : designer.getOnlinePrice();

        // 6. 예약 엔티티 생성 및 저장
        Reservation reservation = Reservation.builder()
                .user(user)
                .designer(designer)
                .type(type)
                .reservationDateTime(reservationDateTime)
                .price(price)
                .canceled(false)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        // 7. 결제 계좌 정보 (예시로 상담 유형에 따라 다르게 설정)
        String paymentAccount;
        if (type == ConsultationType.OFFLINE) {
            paymentAccount = "계좌번호: 111-222-333, 은행: 우리은행";
        } else {
            paymentAccount = "계좌번호: 444-555-666, 은행: 신한은행";
        }

        return new ReservationResponseDto(
                savedReservation.getId(),
                designer.getName(),
                reservationDateTime,
                type,
                price,
                paymentAccount
        );
    }

    public List<Reservation> getReservationsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다: " + userEmail));
        return reservationRepository.findByUserId(user.getId());
    }

    public Reservation getReservationDetail(Long reservationId, String userEmail) {
        Reservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다: " + reservationId));

        if (!res.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("본인의 예약만 조회 가능합니다.");
        }
        return res;
    }

    public void cancelReservation(Long reservationId, String userEmail) {
        Reservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다: " + reservationId));

        if (!res.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("본인의 예약만 취소 가능합니다.");
        }

        res.setCanceled(true);
        reservationRepository.save(res);
    }
}
