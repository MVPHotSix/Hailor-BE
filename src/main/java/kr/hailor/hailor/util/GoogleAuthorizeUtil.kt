package kr.hailor.hailor.util

import kr.hailor.hailor.config.properties.OAuthProperties
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GoogleAuthorizeUtil(
    private val oAuthProperties: OAuthProperties,
) {
    fun getAccessKeyWithCredentials(authCode: String): String? {
        val url = "https://oauth2.googleapis.com/token"

        val requestBody =
            mapOf(
                "client_id" to oAuthProperties.clientId,
                "client_secret" to oAuthProperties.clientSecret,
                "code" to authCode,
                "grant_type" to "authorization_code",
                "redirect_uri" to "https://hailor-api.jayden-bin.kro.kr/Callback",
            )

        val response =
            RestClient
                .create(url)
                .post()
                .body(requestBody)
                .retrieve()
                .body(Map::class.java)

        return response?.get("access_token") as? String
    }
}
