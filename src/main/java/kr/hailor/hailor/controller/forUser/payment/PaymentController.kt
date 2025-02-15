package kr.hailor.hailor.controller.forUser.payment

import io.swagger.v3.oas.annotations.Parameter
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.service.PaymentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentService,
) {
    @PostMapping("/kakao-pay")
    fun requestKakaoPayPayment(
        @Parameter(hidden = true) user: User,
        request: KakaoPayPaymentRequest,
    ): KakaoPayPaymentRequestResponse = KakaoPayPaymentRequestResponse.of(paymentService.requestKakaoPayPayment(request, user))

    @PostMapping("/kakao-pay/confirm")
    fun confirmKakaoPayPayment(
        @Parameter(hidden = true) user: User,
        request: KakaoPayPaymentConfirmRequest,
    ): KakaoPayPaymentConfirmResponse = KakaoPayPaymentConfirmResponse.of(paymentService.confirmKakaoPayPayment(request, user))
}
