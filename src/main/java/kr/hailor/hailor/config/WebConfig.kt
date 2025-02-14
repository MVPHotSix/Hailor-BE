package kr.hailor.hailor.config

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import kr.hailor.hailor.util.UserArgumentResolver
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.time.format.DateTimeFormatter
import java.util.Locale

@Configuration
class WebConfig(
    private val userArgumentResolver: UserArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**") // 모든 엔드포인트에 대해 CORS 허용
            .allowedOrigins("*") // 모든 도메인 허용
            .allowedMethods("*") // 모든 HTTP 메서드 허용
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(false)
    }

    @Bean
    fun jsonCustomizer() =
        Jackson2ObjectMapperBuilderCustomizer {
            val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"
            it.simpleDateFormat(dateTimeFormat)
            it.serializers(LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
            it.deserializers(LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
        }

    @Bean
    fun localeResolver() =
        SessionLocaleResolver().apply {
            setDefaultLocale(Locale.KOREA)
        }
}
