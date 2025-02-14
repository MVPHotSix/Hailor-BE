package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.enity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {
    fun findByDesignerIdAndReservationDate(
        designerId: Long,
        reservationDate: LocalDate,
    ): List<Reservation>

    fun existsByDesignerIdAndReservationDateAndSlot(
        designerId: Long,
        reservationDate: LocalDate,
        slot: Int,
    ): Boolean

    fun findAllByIdLessThanAndUserOrderByIdDesc(
        lastId: Long,
        user: User,
        pageable: Pageable,
    ): Page<Reservation>

    fun findAllByUserOrderByIdDesc(
        user: User,
        pageable: Pageable,
    ): Page<Reservation>
}
