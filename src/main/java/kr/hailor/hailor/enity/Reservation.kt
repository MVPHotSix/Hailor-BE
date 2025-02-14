package kr.hailor.hailor.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
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
    val id: Long,
    @Column(nullable = false)
    val reservationDate: LocalDate,
    @Column(nullable = false)
    val slot: Int,
    @ManyToOne
    val designer: Designer,
    @ManyToOne
    val user: User,
) : BaseModifiableEntity()
