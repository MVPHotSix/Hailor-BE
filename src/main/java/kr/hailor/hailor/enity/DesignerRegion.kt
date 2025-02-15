package kr.hailor.hailor.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "designer_regions")
@SequenceGenerator(name = "DESIGNER_REGION_SEQ_GENERATOR", sequenceName = "DESIGNER_REGION_SEQ", initialValue = 1, allocationSize = 1)
class DesignerRegion(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESIGNER_REGION_SEQ_GENERATOR")
    @Column(updatable = false, nullable = false)
    val id: Long = 0L,
    @Column(length = 50, nullable = false)
    val name: String,
) : BaseModifiableEntity()
