package kr.hailor.hailor.controller.forUser.designer

import kr.hailor.hailor.enity.MeetingType
import java.time.LocalDate

data class DesignerSearchRequest(
    val size: Int,
    val lastId: Long?,
    val name: String?,
    val meetingType: MeetingType?,
    val regionId: Long?,
    val date: LocalDate?,
    val priceMin: Int?,
    val priceMax: Int?,
)
