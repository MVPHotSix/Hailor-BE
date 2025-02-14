package kr.hailor.hailor.config

import kr.hailor.hailor.security.filter.JwtFilter
import kr.hailor.hailor.service.CustomUserDetailService
import kr.hailor.hailor.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val customUserDetailService: CustomUserDetailService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS,
                )
            }.addFilterBefore(
                JwtFilter(jwtUtil, customUserDetailService),
                UsernamePasswordAuthenticationFilter::class.java,
            ).authorizeHttpRequests {
                it
                    .requestMatchers(*PERMITTED_URL_PATTERNS)
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.build()

    companion object {
        private val PERMITTED_URL_PATTERNS =
            arrayOf(
                "/health",
                "/ready",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/users",
                "/api/v1/users/**",
                "/api/v1/terms",
                "/api/v1/auth/sign-up",
                "/api/v1/auth/login",
            )
    }
}
