package kr.hailor.hailor.controller.forUser.reservation

import kr.hailor.hailor.enity.Designer
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.PaymentMethod
import kr.hailor.hailor.enity.ReservationStatus
import java.time.LocalDate

data class UserReservationsResponse(
    val reservations: List<ReservationInfoDto>,
)

data class ReservationInfoDto(
    val id: Long,
    val date: LocalDate,
    val slot: Int,
    val status: ReservationStatus,
    val meetingType: MeetingType,
    val paymentMethod: PaymentMethod,
    val googleMeetLink: String?,
    val price: Int,
    val designer: ReservationDesignerInfoDto,
) {
    data class ReservationDesignerInfoDto(
        val id: Long,
        val name: String,
        val shopAddress: String,
        val region: String,
        val description: String,
        val profileImageURL: String,
    ) {
        companion object {
            fun of(
                designer: Designer,
                profileImageURL: String,
            ): ReservationDesignerInfoDto =
                ReservationDesignerInfoDto(
                    id = designer.id,
                    name = designer.name,
                    shopAddress = designer.shopAddress,
                    region = designer.region.name,
                    description = designer.description,
                    profileImageURL,
                )
        }
    }
}
