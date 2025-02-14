package kr.hailor.hailor.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import java.lang.reflect.Type

// Swagger 에서 multipart/form-data 사용 시 OCTET_STREAM 으로 처리되는 문제 해결
@Component
class MultipartJackson2HttpMessageConverter(
    objectMapper: ObjectMapper,
) : AbstractJackson2HttpMessageConverter(objectMapper, MediaType.APPLICATION_OCTET_STREAM) {
    override fun canWrite(
        clazz: Class<*>,
        mediaType: MediaType?,
    ): Boolean = false

    override fun canWrite(
        type: Type?,
        clazz: Class<*>,
        mediaType: MediaType?,
    ): Boolean = false

    override fun canWrite(mediaType: MediaType?): Boolean = false
}
