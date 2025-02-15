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

@Entity
@Table(name = "designers")
@SequenceGenerator(name = "DESIGNER_SEQ_GENERATOR", sequenceName = "DESIGNER_SEQ", initialValue = 1, allocationSize = 1)
class Designer(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESIGNER_SEQ_GENERATOR")
    @Column(updatable = false, nullable = false)
    val id: Long = 0L,
    @Column(length = 20, nullable = false)
    val name: String,
    @Column(length = 255, nullable = false)
    val shopAddress: String,
    @ManyToOne
    val region: DesignerRegion,
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    val specialization: Specialization,
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val meetingType: MeetingType,
    @Column(nullable = false)
    val offlinePrice: Int,
    @Column(nullable = false)
    val onlinePrice: Int,
    @Column(length = 255, nullable = false)
    val description: String,
    @Column(length = 50, nullable = false)
    val profileImageName: String,
) : BaseModifiableEntity()

enum class Specialization(
    val description: String,
) {
    PERM("펌"),
    DYEING("염색"),
    DYEING_BLEACHING("탈염색"),
}

enum class MeetingType(
    val description: String,
) {
    OFFLINE("대면"),
    ONLINE("비대면"),
    OFFLINE_AND_ONLINE("대면/비대면"),
}
