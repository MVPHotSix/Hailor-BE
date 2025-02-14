package kr.hailor.hailor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
data class OAuthProperties(
    val clientId: String,
    val clientSecret: String,
)
