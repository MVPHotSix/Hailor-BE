package kr.hailor.hailor.dto;

import lombok.*;
import kr.hailor.hailor.entity.Designer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignerDetailResponseDto {

    private Long id;
    private String name;
    private String hairShopAddress;
    private String region;
    private String specialty;
    private int offlinePrice;
    private int onlinePrice;
    private String consultationOption;
    private String shortDescription;
    private String profilePicture;

    // 추가: 각 옵션별 상세 메시지 (상수 값 Designer 클래스에서 가져옴)
    private String offlineMessage;
    private String onlineMessage;
    private String commonMessage;

    // 엔티티 -> DTO 변환 메서드
    public static DesignerDetailResponseDto fromEntity(Designer designer) {
        return DesignerDetailResponseDto.builder()
                .id(designer.getId())
                .name(designer.getName())
                .hairShopAddress(designer.getHairShopAddress())
                .region(designer.getRegion())
                .specialty(designer.getSpecialty())
                .offlinePrice(designer.getOfflinePrice())
                .onlinePrice(designer.getOnlinePrice())
                .consultationOption(designer.getConsultationOption())
                .shortDescription(designer.getShortDescription())
                .profilePicture(designer.getProfilePicture())
                .offlineMessage(Designer.OFFLINE_CONSULTING_MESSAGE)
                .onlineMessage(Designer.ONLINE_CONSULTING_MESSAGE)
                .commonMessage(Designer.COMMON_CONSULTING_MESSAGE)
                .build();
    }
}
