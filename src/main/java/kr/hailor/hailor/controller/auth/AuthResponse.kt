package kr.hailor.hailor.controller.auth

data class ServiceTokensResponse(
    val accessToken: String,
    val refreshToken: String,
)
