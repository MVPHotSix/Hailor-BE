package kr.hailor.hailor.service

import kr.hailor.hailor.client.KakaoPayClient
import kr.hailor.hailor.client.KakaoPayGetOrderStatusResponse
import kr.hailor.hailor.client.KakaoPayReadyResponse
import kr.hailor.hailor.client.KakaoPayStatus
import kr.hailor.hailor.controller.forUser.payment.KakaoPayPaymentConfirmRequest
import kr.hailor.hailor.controller.forUser.payment.KakaoPayPaymentRequest
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.PaymentMethod
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.exception.AlreadyPaidException
import kr.hailor.hailor.exception.InvalidMeetingTypeException
import kr.hailor.hailor.exception.PaymentTypeMismatchException
import kr.hailor.hailor.exception.ReservationNotFoundException
import kr.hailor.hailor.repository.ReservationRepository
import kr.hailor.hailor.util.GoogleMeetCreator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val kakaoPayClient: KakaoPayClient,
    private val reservationRepository: ReservationRepository,
    private val googleMeetCreator: GoogleMeetCreator,
) {
    @Transactional
    fun requestKakaoPayPayment(
        request: KakaoPayPaymentRequest,
        user: User,
    ): KakaoPayReadyResponse {
        val reservation = reservationRepository.findById(request.reservationId).orElseThrow { ReservationNotFoundException() }
        if (reservation.user.id != user.id) {
            throw ReservationNotFoundException()
        }
        if (reservation.paymentMethod != PaymentMethod.KAKAO_PAY) {
            throw PaymentTypeMismatchException()
        }
        if (reservation.status != ReservationStatus.RESERVED) {
            throw AlreadyPaidException()
        }

        val result = kakaoPayClient.ready(amount = reservation.price, orderId = reservation.id, userId = user.id)
        reservation.paymentId = result.tid
        return result
    }

    @Transactional
    fun confirmKakaoPayPayment(
        request: KakaoPayPaymentConfirmRequest,
        user: User,
    ): KakaoPayGetOrderStatusResponse {
        val reservation = reservationRepository.findById(request.reservationId).orElseThrow { ReservationNotFoundException() }
        if (reservation.user.id != user.id) {
            throw ReservationNotFoundException()
        }
        if (reservation.paymentMethod != PaymentMethod.KAKAO_PAY) {
            throw PaymentTypeMismatchException()
        }
        if (reservation.status != ReservationStatus.RESERVED) {
            throw AlreadyPaidException()
        }
        val tid = reservation.paymentId ?: throw ReservationNotFoundException()

        kakaoPayClient.approve(tid, reservation.id, user.id, request.pgToken)

        val result = kakaoPayClient.getOrderStatus(tid)
        if (result.status == KakaoPayStatus.SUCCESS_PAYMENT) {
            reservation.status = ReservationStatus.CONFIRMED
        }
        if (request.googleAccessToken == null) {
            if (reservation.meetingType == MeetingType.ONLINE) {
                throw InvalidMeetingTypeException()
            }
            return result
        } else if (reservation.meetingType == MeetingType.OFFLINE) {
            throw InvalidMeetingTypeException()
        }

        if (result.status != KakaoPayStatus.SUCCESS_PAYMENT) {
            val googleMeetLink =
                googleMeetCreator.createGoogleMeet(reservation, request.googleAccessToken)
            reservation.googleMeetLink = googleMeetLink
        }
        return result
    }

    @Transactional
    fun confirmPayment(reservationId: Long) {
        val reservation = reservationRepository.findById(reservationId).orElseThrow { ReservationNotFoundException() }
        if (reservation.status == ReservationStatus.RESERVED) {
            reservation.status = ReservationStatus.CONFIRMED
        } else {
            throw AlreadyPaidException()
        }
    }
}
