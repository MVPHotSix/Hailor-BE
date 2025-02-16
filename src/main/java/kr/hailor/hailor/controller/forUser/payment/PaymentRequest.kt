package kr.hailor.hailor.controller.forUser.payment

data class KakaoPayPaymentRequest(
    val reservationId: Long,
)

data class KakaoPayPaymentConfirmRequest(
    val reservationId: Long,
    val googleAccessToken: String?,
    val pgToken: String,
)
