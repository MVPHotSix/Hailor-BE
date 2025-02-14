package kr.hailor.hailor.controller.forAdmin.reservation

import kr.hailor.hailor.enity.Designer
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.PaymentMethod
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.enity.User
import java.time.LocalDate

data class AdminReservationListResponse(
    val reservations: List<ReservationInfoDto>,
) {
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
        val user: ReservationUserInfoDto,
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

        data class ReservationUserInfoDto(
            val id: Long,
            val name: String,
            val email: String,
        ) {
            companion object {
                fun of(user: User): ReservationUserInfoDto =
                    ReservationUserInfoDto(
                        id = user.id,
                        name = user.name,
                        email = user.email,
                    )
            }
        }
    }
}
