package kr.hailor.hailor.controller.forAdmin.designer

import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.Specialization

data class AdminDesignerRegionCreateRequest(
    val name: String,
)

data class AdminDesignerCreateRequest(
    val name: String,
    val regionId: Long,
    val meetingType: MeetingType,
    val shopAddress: String,
    val specialization: Specialization,
    val offlinePrice: Int,
    val onlinePrice: Int,
    val description: String,
)

data class AdminDesignerSearchRequest(
    val size: Int,
    val lastId: Long?,
)
