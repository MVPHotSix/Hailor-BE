package kr.hailor.hailor.client

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.hailor.hailor.config.properties.HostProperties
import kr.hailor.hailor.config.properties.KakaoPayProperties
import kr.hailor.hailor.exception.FailToRequestKakaoPayAPIException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class KakaoPayClient(
    val kakaoPayProperties: KakaoPayProperties,
    val hostProperties: HostProperties,
) {
    private val client: RestClient by lazy {
        RestClient
            .builder()
            .baseUrl(hostProperties.kakaoPay)
            .defaultHeader("Authorization", "SECRET_KEY ${kakaoPayProperties.secretKey}")
            .build()
    }

    fun ready(
        amount: Int,
        orderId: Long,
        userId: Long,
    ): KakaoPayReadyResponse =
        client
            .post()
            .uri("/online/v1/payment/ready")
            .body(
                KakaoPayReadyRequest(
                    cid = kakaoPayProperties.cid,
                    partnerOrderId = orderId.toString(),
                    partnerUserId = userId.toString(),
                    itemName = "헤일러 상담비용",
                    quantity = 1,
                    totalAmount = amount,
                    taxFreeAmount = 0,
                    approvalUrl = "${hostProperties.fe}/payment/success",
                    cancelUrl = "${hostProperties.fe}/payment/cancel",
                    failUrl = "${hostProperties.fe}/payment/failure",
                ),
            ).retrieve()
            .body<KakaoPayReadyResponse>() ?: throw FailToRequestKakaoPayAPIException("결제 요청 API")

    fun approve(
        tid: String,
        orderId: Long,
        userId: Long,
        pgToken: String,
    ): String? =
        client
            .post()
            .uri("/online/v1/payment/approve")
            .body(
                KakaoPayApprovalRequest(
                    cid = kakaoPayProperties.cid,
                    tid = tid,
                    partnerOrderId = orderId.toString(),
                    partnerUserId = userId.toString(),
                    pgToken = pgToken,
                ),
            ).retrieve()
            .body(String::class.java)

    fun getOrderStatus(tid: String): KakaoPayGetOrderStatusResponse =
        client
            .post()
            .uri("/online/v1/payment/order")
            .body(
                KakaoPayGetOrderStatusRequest(
                    cid = kakaoPayProperties.cid,
                    tid = tid,
                ),
            ).retrieve()
            .body<KakaoPayGetOrderStatusResponse>() ?: throw FailToRequestKakaoPayAPIException("결제 결과 조회 API")

    fun cancel(
        tid: String,
        amount: Int,
    ) = client
        .post()
        .uri("/online/v1/payment/cancel")
        .body(
            KakaoPayCancelRequest(
                cid = kakaoPayProperties.cid,
                tid = tid,
                cancelAmount = amount,
                cancelTaxFreeAmount = 0,
            ),
        ).retrieve()
        .body<KakaoPayCancelResponse>() ?: throw FailToRequestKakaoPayAPIException("결제 취소 API")
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayReadyRequest(
    val cid: String,
    val partnerOrderId: String,
    val partnerUserId: String,
    val itemName: String,
    val quantity: Int,
    val totalAmount: Int,
    val taxFreeAmount: Int,
    val approvalUrl: String,
    val cancelUrl: String,
    val failUrl: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayReadyResponse(
    val tid: String,
    val nextRedirectPcUrl: String,
    val nextRedirectMobileUrl: String,
    val createdAt: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayApprovalRequest(
    val cid: String,
    val tid: String,
    val partnerOrderId: String,
    val partnerUserId: String,
    val pgToken: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayGetOrderStatusRequest(
    val cid: String,
    val tid: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayGetOrderStatusResponse(
    val tid: String,
    val status: KakaoPayStatus,
    val approvedAt: String?,
    val canceledAt: String?,
)

enum class KakaoPayStatus(
    val description: String,
) {
    READY("결제 요청"),
    SEND_TMS("결제 요청 메시지(TMS) 발송 완료"),
    OPEN_PAYMENT("사용자가 카카오페이 결제 화면 진입"),
    SELECT_METHOD("결제 수단 선택, 인증 완료"),
    ARS_WAITING("ARS 인증 진행 중"),
    AUTH_PASSWORD("비밀번호 인증 완료"),
    ISSUED_SID("SID 발급 완료. 정기 결제 시 SID만 발급 한 경우"),
    SUCCESS_PAYMENT("결제 완료"),
    PART_CANCEL_PAYMENT("부분 취소"),
    CANCEL_PAYMENT("결제된 금액 모두 취소.  부분 취소 여러 번으로 모두 취소된 경우 포함"),
    FAIL_AUTH_PASSWORD("사용자 비밀번호 인증 실패"),
    QUIT_PAYMENT("사용자가 결제 중단"),
    FAIL_PAYMENT("결제 승인 실패"),
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayCancelRequest(
    val cid: String,
    val tid: String,
    val cancelAmount: Int,
    val cancelTaxFreeAmount: Int,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoPayCancelResponse(
    val tid: String,
    val status: KakaoPayStatus,
    val approvedAt: String?,
    val canceledAt: String?,
)
