package kr.hailor.hailor.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "reservation")
@SequenceGenerator(name = "RESERVATION_SEQ_GENERATOR", sequenceName = "RESERVATION_SEQ", initialValue = 1, allocationSize = 1)
class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVATION_SEQ_GENERATOR")
    @Column(updatable = false, nullable = false)
    val id: Long = 0,
    @ManyToOne
    val designer: Designer,
    @ManyToOne
    val user: User,
    @Column(nullable = false)
    val reservationDate: LocalDate,
    @Column(nullable = false)
    val slot: Int,
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val meetingType: MeetingType,
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    var status: ReservationStatus,
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    var paymentMethod: PaymentMethod,
    @Column(length = 100)
    var googleMeetLink: String? = null,
    @Column(length = 50)
    var googleCalendarEventId: String? = null,
    @Column(nullable = false, updatable = false)
    val price: Int,
    @Column(length = 100)
    var paymentId: String? = null,
) : BaseModifiableEntity()

enum class ReservationStatus(
    val description: String,
) {
    RESERVED("예약됨"),
    CONFIRMED("확정됨"),
    FINISHED("종료됨"),
    CANCELED("취소됨"),
    NEED_REFUND("환불 요청됨"),
    REFUNDED("환불됨"),
}

enum class PaymentMethod {
    KAKAO_PAY,
    BANK_TRANSFER,
}
