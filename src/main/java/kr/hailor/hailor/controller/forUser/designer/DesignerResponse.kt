package kr.hailor.hailor.controller.forUser.designer

import kr.hailor.hailor.enity.Designer

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
