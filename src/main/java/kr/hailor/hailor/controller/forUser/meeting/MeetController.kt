package kr.hailor.hailor.controller.forUser.meeting

import io.swagger.v3.oas.annotations.Parameter
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.service.MeetService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/meet")
class MeetController(
    private val meetService: MeetService,
) {
    @PostMapping
    fun createGoogleMeetLink(
        @Parameter(hidden = true)
        user: User,
        @RequestBody request: GoogleMeetCreateRequest,
    ): GoogleMeetCreateResponse = GoogleMeetCreateResponse(meetService.createGoogleMeetLink(user, request))
}
