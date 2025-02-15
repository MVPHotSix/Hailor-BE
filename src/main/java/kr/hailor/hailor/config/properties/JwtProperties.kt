package kr.hailor.hailor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
    val accessKey: String,
    val refreshKey: String,
    val accessTokenExpirationTime: Long,
    val refreshTokenExpirationTime: Long,
)
