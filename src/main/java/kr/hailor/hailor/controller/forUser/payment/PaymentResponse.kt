package kr.hailor.hailor.controller.forUser.payment

import kr.hailor.hailor.client.KakaoPayGetOrderStatusResponse
import kr.hailor.hailor.client.KakaoPayReadyResponse
import kr.hailor.hailor.client.KakaoPayStatus

data class KakaoPayPaymentRequestResponse(
    val nextRedirectPcUrl: String,
    val nextRedirectMobileUrl: String,
) {
    companion object {
        fun of(kakaoPayReadyResponse: KakaoPayReadyResponse) =
            KakaoPayPaymentRequestResponse(
                nextRedirectPcUrl = kakaoPayReadyResponse.nextRedirectPcUrl,
                nextRedirectMobileUrl = kakaoPayReadyResponse.nextRedirectMobileUrl,
            )
    }
}

data class KakaoPayPaymentConfirmResponse(
    val paid: Boolean,
) {
    companion object {
        fun of(kakaoPayGetOrderStatusResponse: KakaoPayGetOrderStatusResponse): KakaoPayPaymentConfirmResponse =
            KakaoPayPaymentConfirmResponse(
                paid = kakaoPayGetOrderStatusResponse.status == KakaoPayStatus.SUCCESS_PAYMENT,
            )
    }
}
