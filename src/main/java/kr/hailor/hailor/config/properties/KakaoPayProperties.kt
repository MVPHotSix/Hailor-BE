package kr.hailor.hailor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kakao-pay")
data class KakaoPayProperties(
    val secretKey: String,
    val cid: String,
)
