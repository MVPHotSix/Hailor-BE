package kr.hailor.hailor.controller.forUser.designer

import kr.hailor.hailor.enity.Designer
import java.time.LocalDate

data class DesignerSearchResponse(
    val designers: List<DesignerInfoDto>,
)

data class DesignerInfoDto(
    val id: Long,
    val name: String,
    val shopAddress: String,
    val region: String,
    val specialization: String,
    val meetingType: String,
    val offlinePrice: Int,
    val onlinePrice: Int,
    val description: String,
    val profileImageURL: String,
) {
    companion object {
        fun of(
            designer: Designer,
            profileImageURL: String,
        ): DesignerInfoDto =
            DesignerInfoDto(
                id = designer.id,
                name = designer.name,
                shopAddress = designer.shopAddress,
                region = designer.region.name,
                specialization = designer.specialization.description,
                meetingType = designer.meetingType.description,
                offlinePrice = designer.offlinePrice,
                onlinePrice = designer.onlinePrice,
                description = designer.description,
                profileImageURL,
            )
    }
}

data class DesignerRegionResponse(
    val id: Long,
    val name: String,
)

data class DesignerScheduleResponse(
    val date: LocalDate,
    val designer: DesignerInfoDto,
    val schedule: DesignerScheduleInfoDto,
)

data class DesignerScheduleInfoDto(
    val slot: List<Int>,
)

data class PopularDesignerResponse(
    val designers: List<DesignerInfoDto>,
)
