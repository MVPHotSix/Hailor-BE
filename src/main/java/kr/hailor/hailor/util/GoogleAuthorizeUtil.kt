package kr.hailor.hailor.util

import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import kr.hailor.hailor.config.properties.HostProperties
import org.springframework.stereotype.Component

@Component
class GoogleAuthorizeUtil(
    private val hostProperties: HostProperties,
) {
    companion object {
        private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
        private const val TOKENS_DIRECTORY_PATH = "tokens"
    }
}
