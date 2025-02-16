package kr.hailor.hailor.controller.forUser.meeting

data class GoogleMeetCreateRequest(
    val googleAuthCode: String,
    val reservationId: Long,
)
