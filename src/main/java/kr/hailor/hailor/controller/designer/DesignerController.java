package kr.hailor.hailor.controller.designer;

import kr.hailor.hailor.dto.DesignerDetailResponseDto;
import kr.hailor.hailor.entity.Designer;
import kr.hailor.hailor.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/designers")
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerRepository designerRepository;

    // 전체 디자이너 목록 조회 (예약 신청 시, 사용자에게 리스트를 보여주기 위해 사용)
    @GetMapping
    public List<DesignerDetailResponseDto> getAllDesigners() {
        List<Designer> designers = designerRepository.findAll();
        return designers.stream()
                .map(DesignerDetailResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 단일 디자이너 상세 조회 (필요 시 디자이너 상세 정보를 보여줄 때 사용)
    @GetMapping("/{id}")
    public DesignerDetailResponseDto getDesigner(@PathVariable("id") Long id) {
        Designer designer = designerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("디자이너가 존재하지 않습니다: ID=" + id));
        return DesignerDetailResponseDto.fromEntity(designer);
    }

}
