package kr.hailor.hailor.controller.forUser.meeting

data class GoogleMeetCreateRequest(
    val token: String,
    val reservationId: Long,
)
