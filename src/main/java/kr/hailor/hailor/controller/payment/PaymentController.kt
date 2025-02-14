package kr.hailor.hailor.controller.payment

import kr.hailor.hailor.service.PaymentService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentService,
) {
    @PostMapping("/kakao-pay")
    fun requestKakaoPayPayment(request: KakaoPayPaymentRequest): KakaoPayPaymentRequestResponse =
        KakaoPayPaymentRequestResponse.of(paymentService.requestKakaoPayPayment(request))

    @PostMapping("/kakao-pay/confirm")
    fun confirmKakaoPayPayment(request: KakaoPayPaymentConfirmRequest): KakaoPayPaymentConfirmResponse =
        KakaoPayPaymentConfirmResponse.of(paymentService.confirmKakaoPayPayment(request))
}
