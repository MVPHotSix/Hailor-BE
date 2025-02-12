package kr.hailor.hailor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "designers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 디자이너 이름 (예: "이초 디자이너")
    @Column(nullable = false)
    private String name;

    // 헤어샵 주소 (예: "서울 강남구 압구정로79길")
    private String hairShopAddress;

    // 지역 (예: "강남/청담/압구정")
    private String region;

    // 전문 분야 (예: "펌")
    private String specialty;

    // 대면 컨설팅 금액 (예: 40000)
    private int offlinePrice;

    // 비대면 컨설팅 금액 (예: 20000)
    private int onlinePrice;

    // 대면/비대면 옵션 (예: "대면, 비대면")
    private String consultationOption;

    // 한 줄 소개 (띄어쓰기 포함 최대 30자)
    @Column(length = 30)
    private String shortDescription;

    // 추가: 프로필 사진 URL
    private String profilePicture;

    // 대면 선택 시 상세 메시지 (상수로 정의하여 DTO 전달 시 사용)
    public static final String OFFLINE_CONSULTING_MESSAGE =
            "30,000₩ 부터 시작\n실제 샵에 방문하여 컨설팅 진행";

    // 화상(비대면) 선택 시 상세 메시지
    public static final String ONLINE_CONSULTING_MESSAGE =
            "20,000₩ 부터 시작\n예약 완료 후 생성되는 구글미트에서 화상으로 컨설팅 진행";

    // 공통 컨설팅 안내 메시지
    public static final String COMMON_CONSULTING_MESSAGE =
            "소요시간 약 30분 진행\n컨설팅 내용은 진행 후 요약된 리포트 고객에게 전달됩니다";
}
