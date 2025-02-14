package kr.hailor.hailor.util

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import kr.hailor.hailor.config.properties.HostProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStreamReader

@Component
class GoogleAuthorizeUtil(
    private val hostProperties: HostProperties,
) {
    companion object {
        private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
        private const val TOKENS_DIRECTORY_PATH = "tokens"
    }

    fun authorize(): Credential {
        val inputStream = ClassPathResource("credentials.json").inputStream
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        val flow =
            GoogleAuthorizationCodeFlow
                .Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    clientSecrets,
                    listOf(
                        "https://www.googleapis.com/auth/calendar.app.created",
                        "https://www.googleapis.com/auth/calendar.calendarlist.readonly",
                    ),
                ).setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
        val receiver =
            LocalServerReceiver
                .Builder()
                .setHost(hostProperties.apiServer)
                .setPort(443)
                .build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user") // TODO user 부분은 사용자 유니크키로 잡아야 할듯
    }
}
