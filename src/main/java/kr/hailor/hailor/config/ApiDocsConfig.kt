package kr.hailor.hailor.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import kr.hailor.hailor.enity.User
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION

@Configuration
class ApiDocsConfig {
    init {
        SpringDocUtils.getConfig().addJavaTypeToIgnore(User::class.java)
    }

    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(createApiInfo())
            .components(createSecurityComponents())
            .addSecurityItem(createSecurityRequirement())

    private fun createApiInfo(): Info =
        Info()
            .title("Hailor API")
            .description("Hailor API 문서입니다.")
            .version("1.0.0")

    private fun createSecurityComponents(): Components =
        Components()
            .addSecuritySchemes(
                AUTHORIZATION,
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .`in`(SecurityScheme.In.HEADER)
                    .name(AUTHORIZATION),
            )

    private fun createSecurityRequirement(): SecurityRequirement =
        SecurityRequirement()
            .addList(AUTHORIZATION)
}
