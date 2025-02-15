package kr.hailor.hailor.controller.forUser.reservation

import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.PaymentMethod
import java.time.LocalDate

data class ReservationCreateRequest(
    val designerId: Long,
    val reservationDate: LocalDate,
    val slot: Int,
    val meetingType: MeetingType,
    val paymentMethod: PaymentMethod,
)

data class UserReservationsSearchRequest(
    val size: Int,
    val lastId: Long?,
)
