package kr.hailor.hailor.service

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.ConferenceData
import com.google.api.services.calendar.model.ConferenceSolutionKey
import com.google.api.services.calendar.model.CreateConferenceRequest
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kr.hailor.hailor.util.GoogleAuthorizeUtil
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GoogleCalendarService(
    private val googleAuthorizeUtil: GoogleAuthorizeUtil,
) {
    fun createGoogleMeetLink(): String {
        val service =
            Calendar
                .Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    googleAuthorizeUtil.authorize(),
                ).setApplicationName("Hailor")
                .build()

        val event =
            Event()
                .setSummary("Hailor 컨설팅 예약")
                .setDescription("고객님과 디자이너님의 소중한 컨설팅 시간입니다")

        // TODO 여기 시간은 상담 시간에 맞게 고쳐야 할듯
        val startDateTime = DateTime("2025-02-15T10:00:00+09:00")
        val start = EventDateTime().setDateTime(startDateTime).setTimeZone("Asia/Seoul")
        event.start = start

        // TODO 여기도
        val endDateTime = DateTime("2025-02-15T11:00:00+09:00")
        val end = EventDateTime().setDateTime(endDateTime).setTimeZone("Asia/Seoul")
        event.end = end

        val conferenceData =
            ConferenceData().apply {
                createRequest =
                    CreateConferenceRequest().apply {
                        requestId = UUID.randomUUID().toString()
                        conferenceSolutionKey = ConferenceSolutionKey().apply { type = "hangoutsMeet" }
                    }
            }
        event.conferenceData = conferenceData

        val createdEvent =
            service
                .events()
                .insert("primary", event)
                .setConferenceDataVersion(1)
                .execute()

        return createdEvent.hangoutLink ?: "Google Meet 링크 생성 실패"
    }
}
