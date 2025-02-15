package kr.hailor.hailor.controller.auth

data class LoginRequest(
    val token: String,
)

data class SignUpRequest(
    val agreedTerms: List<Long>,
    val token: String,
)
