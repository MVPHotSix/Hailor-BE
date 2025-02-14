package kr.hailor.hailor.controller.auth

import kr.hailor.hailor.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ) = authService.login(request)

    @GetMapping("/refresh")
    fun refreshToken(
        @RequestParam("token") refreshToken: String,
    ): ServiceTokensResponse = authService.refreshToken(refreshToken)
}
