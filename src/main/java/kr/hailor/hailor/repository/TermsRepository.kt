package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.Terms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TermsRepository : JpaRepository<Terms, Long>
