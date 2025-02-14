package kr.hailor.hailor.controller.forUser.meeting

import kr.hailor.hailor.service.GoogleCalendarService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/meet")
class MeetController(
    private val calendarService: GoogleCalendarService,
) {
    // TODO 지금은 확인을 위해 임시로 만들어두었지만 나중에는 예약 확정시 이 링크를 생성해줘야 한다
    @GetMapping("/create-meet")
    fun createMeetLink(): String = calendarService.createGoogleMeetLink()
}
