package kr.hailor.hailor.controller.forUser.meeting

data class GoogleMeetCreateRequest(
    val googleAccessToken: String,
    val reservationId: Long,
)
