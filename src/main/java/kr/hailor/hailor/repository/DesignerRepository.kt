package kr.hailor.hailor.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import kr.hailor.hailor.controller.forUser.designer.DesignerSearchRequest
import kr.hailor.hailor.enity.Designer
import kr.hailor.hailor.enity.DesignerRegion
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.util.getPage
import kr.hailor.hailor.util.getSlice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DesignerRepository :
    JpaRepository<Designer, Long>,
    DesignerCustomRepository {
    fun findAllByIdLessThanOrderByIdDesc(
        lastId: Long,
        pageable: Pageable,
    ): Page<Designer>

    fun findAllByOrderByIdDesc(pageable: Pageable): Page<Designer>
}

interface DesignerCustomRepository {
    fun searchDesigner(request: DesignerSearchRequest): Slice<Designer>

    fun popularDesigner(targetDate: LocalDate): Page<Designer>
}

class DesignerCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : DesignerCustomRepository {
    override fun searchDesigner(request: DesignerSearchRequest): Slice<Designer> =
        kotlinJdslJpqlExecutor.getSlice(Pageable.ofSize(request.size)) {
            val designerEntity = entity(Designer::class)
            select(designerEntity)
                .from(designerEntity)
                .whereAnd(
                    *buildList {
                        if (request.name != null) {
                            add(path(Designer::name).eq(request.name))
                        }
                        if (request.meetingType != null) {
                            add(path(Designer::meetingType).`in`(listOf(request.meetingType, MeetingType.OFFLINE_AND_ONLINE)))
                        }
                        if (request.regionId != null) {
                            add(path(Designer::region)(DesignerRegion::id).eq(request.regionId))
                        }
                        if (request.date != null) {
                            val reservationEntity = entity(Reservation::class)
                            add(
                                select(count(reservationEntity))
                                    .from(reservationEntity)
                                    .whereAnd(
                                        path(Reservation::designer).eq(designerEntity),
                                        path(Reservation::reservationDate).eq(request.date),
                                    ).asSubquery()
                                    .ne(20), // 하루에 20명까지만 예약 가능
                            )
                        }
                        if (request.priceMin != null && request.priceMax != null) {
                            if (request.meetingType != null) {
                                if (request.meetingType == MeetingType.OFFLINE) {
                                    add(path(Designer::offlinePrice).between(request.priceMin, request.priceMax))
                                } else {
                                    add(path(Designer::onlinePrice).between(request.priceMin, request.priceMax))
                                }
                            } else {
                                add(
                                    path(Designer::offlinePrice).between(request.priceMin, request.priceMax).or(
                                        path(Designer::onlinePrice).between(request.priceMin, request.priceMax),
                                    ),
                                )
                            }
                        }
                        if (request.lastId != null) {
                            add(path(Designer::id).lt(request.lastId))
                        }
                    }.toTypedArray(),
                ).orderBy(path(Designer::id).desc())
        }

    override fun popularDesigner(targetDate: LocalDate): Page<Designer> =
        kotlinJdslJpqlExecutor.getPage(Pageable.ofSize(10)) {
            val designerEntity = entity(Designer::class)
            val reservationEntity = entity(Reservation::class)
            // targetDate 이후 예약된 예약이 가장 많은 디자이너 10명을 조회
            select(designerEntity)
                .from(designerEntity)
                .orderBy(
                    select(count(reservationEntity))
                        .from(reservationEntity)
                        .whereAnd(
                            path(Reservation::reservationDate).ge(targetDate),
                            path(Reservation::designer).eq(designerEntity),
                        ).asSubquery()
                        .desc(),
                )
        }
}
