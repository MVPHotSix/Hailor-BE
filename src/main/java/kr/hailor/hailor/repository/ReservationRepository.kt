package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.enity.ReservationStatus
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

    fun existsByDesignerIdAndReservationDateAndSlotAndStatusIn(
        designerId: Long,
        reservationDate: LocalDate,
        slot: Int,
        statuses: List<ReservationStatus>,
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

    fun findAllByIdLessThanOrderByIdDesc(
        lastId: Long,
        pageable: Pageable,
    ): Page<Reservation>

    fun findAllByOrderByIdDesc(pageable: Pageable): Page<Reservation>
}
