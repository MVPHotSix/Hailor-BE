package kr.hailor.hailor.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import kr.hailor.hailor.controller.forUser.reservation.UserReservationsSearchRequest
import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.util.findList
import kr.hailor.hailor.util.getPage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface ReservationRepository :
    JpaRepository<Reservation, Long>,
    ReservationCustomRepository {
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

interface ReservationCustomRepository {
    fun findAllReservationFinishTarget(targetDateTime: LocalDateTime): List<Reservation>

    fun findAllRecentFinishedReservation(
        user: User,
        request: UserReservationsSearchRequest,
        targetDateTime: LocalDateTime,
    ): Page<Reservation>
}

class ReservationCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : ReservationCustomRepository {
    override fun findAllReservationFinishTarget(targetDateTime: LocalDateTime): List<Reservation> =
        kotlinJdslJpqlExecutor
            .findList {
                val targetDate = targetDateTime.toLocalDate()
                val reservationEntity = entity(Reservation::class)
                val targetSlot = (targetDateTime.hour - 10) / 2 + if (targetDateTime.minute >= 30) 1 else 0
                select(reservationEntity)
                    .from(reservationEntity)
                    .whereAnd(
                        // status가 RESERVED, CONFIRMED이고 targetDateTime 이전인 예약을 조회
                        path(Reservation::status).`in`(listOf(ReservationStatus.RESERVED, ReservationStatus.CONFIRMED)),
                        path(Reservation::reservationDate).lt(targetDate).or(
                            path(Reservation::reservationDate).eq(targetDate).and(
                                path(Reservation::slot).le(targetSlot),
                            ),
                        ),
                    )
            }

    override fun findAllRecentFinishedReservation(
        user: User,
        request: UserReservationsSearchRequest,
        targetDateTime: LocalDateTime,
    ): Page<Reservation> =
        kotlinJdslJpqlExecutor.getPage(Pageable.ofSize(request.size)) {
            val targetDate = targetDateTime.toLocalDate()
            val reservationEntity = entity(Reservation::class)
            val targetSlot = (targetDateTime.hour - 10) / 2 + if (targetDateTime.minute >= 30) 1 else 0
            select(reservationEntity)
                .from(reservationEntity)
                .whereAnd(
                    // status가 RESERVED, CONFIRMED이고 targetDateTime 이전인 예약을 조회
                    path(Reservation::status).`in`(listOf(ReservationStatus.FINISHED)),
                    path(Reservation::reservationDate).lt(targetDate).or(
                        path(Reservation::reservationDate).eq(targetDate).and(
                            path(Reservation::slot).le(targetSlot),
                        ),
                    ),
                    path(Reservation::user).eq(user),
                    request.lastId?.let { path(Reservation::id).lt(request.lastId) },
                )
        }
}
