package kr.hailor.hailor.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health")
    fun health(): HealthResponse {
        return HealthResponse(HttpServletResponse.SC_OK)
    }

    @GetMapping("/ready")
    fun ready() {
    }
}

@JvmRecord
data class HealthResponse(val status: Int)
