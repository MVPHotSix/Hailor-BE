package kr.hailor.hailor.service

import kr.hailor.hailor.controller.forUser.designer.DesignerInfoDto
import kr.hailor.hailor.controller.forUser.designer.PopularDesignerResponse
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.repository.DesignerRepository
import kr.hailor.hailor.repository.ObjectStorageRepository
import kr.hailor.hailor.repository.ReservationRepository
import kr.hailor.hailor.util.LockUtil
import org.redisson.api.RedissonClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Component
class JobScheduler(
    private val designerRepository: DesignerRepository,
    private val reservationRepository: ReservationRepository,
    private val lockUtil: LockUtil,
    private val redissonClient: RedissonClient,
    private val objectStorageRepository: ObjectStorageRepository,
) {
    @Scheduled(cron = "0 */30 * * * *")
    fun reservationFinish() {
        if (lockUtil.lock("reservationFinish", 0, 29, TimeUnit.MINUTES)) {
            val targetDateTime = LocalDateTime.now()
            val reservations = reservationRepository.findAllReservationFinishTarget(targetDateTime)
            reservations.forEach {
                it.status = ReservationStatus.FINISHED
            }
            reservationRepository.saveAll(reservations)
            lockUtil.unlock("reservationFinish")
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun getPopularDesigner(): PopularDesignerResponse? {
        if (lockUtil.lock("popularDesigner", 0, 23, TimeUnit.HOURS)) {
            // 최근 7일까지 포함해서 가장 예약이 많은 디자이너를 조회
            val designers = designerRepository.popularDesigner(LocalDate.now().minusDays(7))
            val response =
                PopularDesignerResponse(
                    designers.content.map { designer ->
                        DesignerInfoDto.of(designer, objectStorageRepository.getDownloadUrl(designer.profileImageName))
                    },
                )
            redissonClient.getBucket<PopularDesignerResponse>(POPULAR_DESIGNER_DATA_CACHE_KEY).set(
                response,
                Duration.of(48, ChronoUnit.HOURS), // 혹시나 모를 캐싱 실패를 막기위해 48시간으로 설정
            )
            return response
        }
        return null
    }

    companion object {
        val POPULAR_DESIGNER_DATA_CACHE_KEY = "popularDesignerData"
    }
}
