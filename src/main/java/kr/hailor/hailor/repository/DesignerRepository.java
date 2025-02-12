package kr.hailor.hailor.repository;

import kr.hailor.hailor.entity.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<Designer, Long> {
    // 필요 시 지역, 가격 등 조건에 따른 쿼리 메소드 추가 가능
}
