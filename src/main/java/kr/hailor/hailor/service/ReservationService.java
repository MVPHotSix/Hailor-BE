package kr.hailor.hailor.service;

import kr.hailor.hailor.dto.ReservationDto;
import kr.hailor.hailor.entity.*;
import kr.hailor.hailor.repository.DesignerRepository;
import kr.hailor.hailor.repository.ReservationRepository;
import kr.hailor.hailor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;

    // [필수] 예약 생성: 대면/비대면 옵션, 디자이너 선택, 예약 날짜/시간을 받아 예약을 생성함
    public Reservation createReservation(String userEmail, ReservationDto dto) {
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

        // 4. 예약 금액 결정 (디자이너의 offlinePrice 또는 onlinePrice 선택)
        int price = (type == ConsultationType.OFFLINE)
                ? designer.getOfflinePrice()
                : designer.getOnlinePrice();

        // 5. (필요 시) 예약 중복 체크 등 추가 로직 구현 가능

        // 6. 예약 엔티티 생성 및 저장
        Reservation reservation = Reservation.builder()
                .user(user)
                .designer(designer)
                .type(type)
                .reservationDateTime(dto.getReservationDateTime())
                .price(price)
                .canceled(false)
                .build();

        return reservationRepository.save(reservation);
    }

    // ----------------------------------------------------------------
    // 아래의 메서드들은 예약 조회/상세/취소 기능으로 현재 작업 범위에 포함되지 않으므로,
    // 추후 개발자가 구현할 때 참고하거나 제거해도 무방합니다.
    // ----------------------------------------------------------------

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
