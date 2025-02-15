package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.DesignerRegion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DesignerRegionRepository : JpaRepository<DesignerRegion, Long>
