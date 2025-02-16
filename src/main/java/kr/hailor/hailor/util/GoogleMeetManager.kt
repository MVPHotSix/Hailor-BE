package kr.hailor.hailor.util

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.ConferenceData
import com.google.api.services.calendar.model.ConferenceSolutionKey
import com.google.api.services.calendar.model.CreateConferenceRequest
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.exception.GoogleMeetLinkException
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Component
class GoogleMeetManager {
    fun createGoogleMeet(
        reservation: Reservation,
        accessToken: String,
    ): Pair<String, String> {
        val startLocalDateTime =
            reservation.reservationDate.atStartOfDay().plusHours((10 + reservation.slot / 2).toLong()).plusMinutes(
                if (reservation.slot % 2 == 0) {
                    0
                } else {
                    30
                },
            )
        val startDateTime = DateTime(Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()))

        val endLocalDateTime = startLocalDateTime.plusMinutes(30)
        val endDateTime = DateTime(Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()))

        val calendarService = getCalendarService(accessToken)

        val event =
            Event()
                .setSummary("Hailor 컨설팅 예약")
                .setDescription("고객님과 디자이너님의 소중한 컨설팅 시간입니다")

        event.start = EventDateTime().setDateTime(startDateTime).setTimeZone("Asia/Seoul")

        event.end = EventDateTime().setDateTime(endDateTime).setTimeZone("Asia/Seoul")
        event.attendees =
            listOf(
                EventAttendee().apply {
                    email = "1117plus@gmail.com" // TODO 나중에는 여기에 디자이너 이메일이 들어가도록 바꿔야함
                    responseStatus = "accepted" // 자동 수락
                },
            )

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
            calendarService
                .events()
                .insert(getOrCreateSecondaryCalendar(calendarService), event)
                .setConferenceDataVersion(1)
                .execute()
        if (createdEvent.id == null || createdEvent.hangoutLink == null) {
            throw GoogleMeetLinkException()
        }

        return Pair(createdEvent.id, createdEvent.hangoutLink)
    }

    fun deleteGoogleMeet(
        eventId: String,
        accessToken: String,
    ) {
        val calendarService = getCalendarService(accessToken)
        calendarService.events().delete(getOrCreateSecondaryCalendar(calendarService), eventId).execute()
    }

    private fun getCalendarService(accessToken: String): Calendar =
        Calendar
            .Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                HttpCredentialsAdapter(GoogleCredentials.newBuilder().setAccessToken(AccessToken(accessToken, null)).build()),
            ).setApplicationName("Hailor")
            .build()

    private fun getOrCreateSecondaryCalendar(calendarService: Calendar): String {
        val calendarSummary = "Hailor 디자이너 예약 캘린더" // 보조 캘린더 이름

        // 보조 캘린더 목록 조회
        val calendarList = calendarService.calendarList().list().execute()
        val existingCalendar = calendarList.items.find { it.summary == calendarSummary }

        return if (existingCalendar != null) {
            existingCalendar.id // 이미 존재하면 해당 ID 반환
        } else {
            val calendar =
                com.google.api.services.calendar.model.Calendar().apply {
                    summary = calendarSummary
                    timeZone = "Asia/Seoul"
                }
            val createdCalendar = calendarService.calendars().insert(calendar).execute()
            println("🆕 보조 캘린더 생성 완료: ${createdCalendar.id}")
            createdCalendar.id
        }
    }
}
