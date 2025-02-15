package kr.hailor.hailor.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "terms")
@SequenceGenerator(name = "TERMS_SEQ_GENERATOR", sequenceName = "TEMRS_SEQ", initialValue = 1, allocationSize = 1)
class Terms(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERMS_SEQ_GENERATOR")
    @Column(updatable = false, nullable = false)
    val id: Long = 0L,
    @Column(length = 100, nullable = false)
    val title: String,
    @Column(nullable = false)
    val isRequired: Boolean,
    @Column(length = 100, nullable = false)
    val contentFileName: String,
)
