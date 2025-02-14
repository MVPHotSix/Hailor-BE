package kr.hailor.hailor.config

import org.hibernate.internal.util.collections.CollectionHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .cors { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS,
                )
            }.authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .requestMatchers(*PERMITTED_URL_PATTERNS)
                    .permitAll()
                    .anyRequest() // .authenticated()
                    .permitAll()
            } // TODO 중요! 로그인이 완성되면 .authenticated()로 변경
            .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins =
            CollectionHelper.listOf("*")
        configuration.allowedMethods =
            CollectionHelper.listOf(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
            )
        configuration.exposedHeaders =
            CollectionHelper.listOf("Authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    companion object {
        private val PERMITTED_URL_PATTERNS =
            arrayOf(
                "/health",
                "/ready",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/users",
                "/api/v1/users/**",
            )
    }
}
