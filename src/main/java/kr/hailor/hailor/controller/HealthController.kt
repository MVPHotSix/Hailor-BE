package kr.hailor.hailor.controller

import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class HealthController {
    @GetMapping("/health")
    fun health(): HealthResponse = HealthResponse(HttpServletResponse.SC_OK)

    @GetMapping("/ready")
    fun ready() {
    }
}

@JvmRecord
data class HealthResponse(
    val status: Int,
)
