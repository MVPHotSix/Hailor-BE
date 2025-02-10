package kr.hailor.hailor.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse(HttpServletResponse.SC_OK);
    }

    @GetMapping("/ready")
    void ready() {
    }
}


record HealthResponse(int status) {
}
