package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.AgreedTerms
import org.springframework.data.jpa.repository.JpaRepository

interface AgreedTermsRepository : JpaRepository<AgreedTerms, Long>
