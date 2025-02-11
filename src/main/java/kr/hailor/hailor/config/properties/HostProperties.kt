package kr.hailor.hailor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "host")
data class HostProperties(
    val apiServer: String,
    val fe: String,
    val kakaoPay: String,
)
