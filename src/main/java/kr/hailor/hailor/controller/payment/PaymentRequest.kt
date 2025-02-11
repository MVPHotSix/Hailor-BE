package kr.hailor.hailor.controller.payment

data class KakaoPayPaymentRequest(
    val reservationId: Long,
)

data class KakaoPayPaymentConfirmRequest(
    val reservationId: Long,
)
