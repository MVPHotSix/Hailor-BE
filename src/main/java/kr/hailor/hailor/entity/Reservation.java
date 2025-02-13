package kr.hailor.hailor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 예약자 (User 엔티티와 연관; 예약 신청 시 JWT 인증된 사용자의 정보가 연결됨)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  // 예약한 디자이너
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "designer_id")
  private Designer designer;

  // 대면/비대면 선택 옵션 (enum 값)
  @Enumerated(EnumType.STRING)
  private ConsultationType type;

  // 예약 날짜 및 시간
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime reservationDateTime;

  // 예약 시 확정된 가격 (디자이너의 offlinePrice 또는 onlinePrice 중 하나)
  private int price;

  // 예약 취소 여부 (추후 예약 취소 기능 구현 시 사용; 현재 예약 신청에는 필요 없으나 남겨둠)
  private boolean canceled;
}