package kr.hailor.hailor.service

import kr.hailor.hailor.client.KakaoPayClient
import kr.hailor.hailor.client.KakaoPayGetOrderStatusResponse
import kr.hailor.hailor.client.KakaoPayReadyResponse
import kr.hailor.hailor.controller.forUser.payment.KakaoPayPaymentConfirmRequest
import kr.hailor.hailor.controller.forUser.payment.KakaoPayPaymentRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val kakoPayClient: KakaoPayClient,
) {
    @Transactional
    fun requestKakaoPayPayment(
        request: KakaoPayPaymentRequest,
        // user: User
    ): KakaoPayReadyResponse {
        // TODO 현진님 작업 끝나면 대충 아래 코드로 실제 예약과 연결 시키면 될듯
        // val reservation = reservationRepository.findById(request.reservationId)
        // val amount = reservation.amount
        // val orderId = reservation.id
        // val userId = user.id
        val result = kakoPayClient.ready(1000, 1, 1)
        // reservation.paymentId = result.tid
        // reservation.paymentType = PaymentType.KAKAO_PAY
        return result
    }

    @Transactional
    fun confirmKakaoPayPayment(request: KakaoPayPaymentConfirmRequest): KakaoPayGetOrderStatusResponse {
        // TODO 현진님 작업 끝나면 대충 아래 코드로 실제 예약과 연결 시키면 될듯
        // val reservation = reservationRepository.findById(request.reservationId)
        // if (reservation.paymentType != PaymentType.KAKAO_PAY) {
        //     throw PaymentTypeMismatchException()
        // } else if (reservation.status == ReservationStatus.PAID) {
        //     throw AlreadyPaidException()
        // }
        // val tid = reservation.paymentId
        val tid = "T7ab55e6222b2ae7db34" // 임시

        val result = kakoPayClient.getOrderStatus(tid)
        // if (result.status == KakaoPayStatus.SUCCESS_PAYMENT) {
        //     reservation.status = ReservationStatus.PAID
        // }
        return result
    }
}
