package kr.hailor.hailor.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "agreed_terms")
@SequenceGenerator(name = "AGREED_TERMS_SEQ_GENERATOR", sequenceName = "AGREED_TERMS_SEQ", initialValue = 1, allocationSize = 1)
class AgreedTerms(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGREED_TERMS_SEQ_GENERATOR")
    @Column(updatable = false, nullable = false)
    val id: Long = 0L,
    @ManyToOne
    val user: User,
    @ManyToOne
    val terms: Terms,
) : BaseTimeEntity()
