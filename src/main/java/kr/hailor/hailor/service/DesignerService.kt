package kr.hailor.hailor.service

import kr.hailor.hailor.controller.forAdmin.designer.AdminDesignerCreateRequest
import kr.hailor.hailor.controller.forAdmin.designer.AdminDesignerRegionCreateRequest
import kr.hailor.hailor.controller.forAdmin.designer.AdminDesignerSearchRequest
import kr.hailor.hailor.controller.forAdmin.designer.AdminDesignerSearchResponse
import kr.hailor.hailor.controller.forUser.designer.DesignerInfoDto
import kr.hailor.hailor.controller.forUser.designer.DesignerRegionResponse
import kr.hailor.hailor.controller.forUser.designer.DesignerScheduleInfoDto
import kr.hailor.hailor.controller.forUser.designer.DesignerScheduleResponse
import kr.hailor.hailor.controller.forUser.designer.DesignerSearchRequest
import kr.hailor.hailor.controller.forUser.designer.DesignerSearchResponse
import kr.hailor.hailor.controller.forUser.designer.PopularDesignerResponse
import kr.hailor.hailor.enity.Designer
import kr.hailor.hailor.enity.DesignerRegion
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.exception.DesignerNotFoundException
import kr.hailor.hailor.exception.NotSupportedImageExtensionException
import kr.hailor.hailor.exception.RegionNotFoundException
import kr.hailor.hailor.exception.TemporallyUnavailableException
import kr.hailor.hailor.repository.DesignerRegionRepository
import kr.hailor.hailor.repository.DesignerRepository
import kr.hailor.hailor.repository.ObjectStorageRepository
import kr.hailor.hailor.repository.ReservationRepository
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.util.UUID

@Service
class DesignerService(
    private val designerRepository: DesignerRepository,
    private val designerRegionRepository: DesignerRegionRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val reservationRepository: ReservationRepository,
    private val redissonClient: RedissonClient,
    private val jobScheduler: JobScheduler,
) {
    fun searchDesigner(request: DesignerSearchRequest): DesignerSearchResponse {
        val designers = designerRepository.searchDesigner(request).content
        return DesignerSearchResponse(
            designers =
                designers.map { designer ->
                    DesignerInfoDto.of(designer, objectStorageRepository.getDownloadUrl(designer.profileImageName))
                },
        )
    }

    fun searchAdminDesigner(request: AdminDesignerSearchRequest): AdminDesignerSearchResponse {
        val designers =
            if (request.lastId == null) {
                designerRepository.findAllByOrderByIdDesc(Pageable.ofSize(request.size)).content
            } else {
                designerRepository.findAllByIdLessThanOrderByIdDesc(request.lastId, Pageable.ofSize(request.size)).content
            }
        return AdminDesignerSearchResponse(
            designers =
                designers.map { designer ->
                    AdminDesignerSearchResponse.DesignerInfoDto.of(
                        designer,
                        objectStorageRepository.getDownloadUrl(designer.profileImageName),
                    )
                },
        )
    }

    fun createRegion(request: AdminDesignerRegionCreateRequest) {
        designerRegionRepository.save(DesignerRegion(name = request.name))
    }

    @Transactional
    fun createDesigner(
        request: AdminDesignerCreateRequest,
        profileImage: MultipartFile,
    ) {
        if (!objectStorageRepository.isImageFile(profileImage)) {
            throw NotSupportedImageExtensionException()
        }
        val fileName = UUID.randomUUID().toString() + '.' + profileImage.originalFilename!!.split(".").last()
        designerRepository.save(
            Designer(
                name = request.name,
                region = designerRegionRepository.findById(request.regionId).orElseThrow { RegionNotFoundException() },
                meetingType = request.meetingType,
                shopAddress = request.shopAddress,
                specialization = request.specialization,
                offlinePrice = request.offlinePrice,
                onlinePrice = request.onlinePrice,
                description = request.description,
                profileImageName = fileName,
            ),
        )
        objectStorageRepository.upload(fileName, profileImage.inputStream)
    }

    fun getRegions(): List<DesignerRegionResponse> = designerRegionRepository.findAll().map { DesignerRegionResponse(it.id, it.name) }

    @Transactional(readOnly = true)
    fun getDesignerSchedule(
        id: Long,
        date: LocalDate,
    ): DesignerScheduleResponse {
        val designer = designerRepository.findById(id).orElseThrow { DesignerNotFoundException() }
        val reservation = reservationRepository.findByDesignerIdAndReservationDate(id, date)
        return DesignerScheduleResponse(
            date = date,
            designer = DesignerInfoDto.of(designer, objectStorageRepository.getDownloadUrl(designer.profileImageName)),
            schedule =
                DesignerScheduleInfoDto(
                    reservation
                        .filter {
                            it.status in
                                listOf(ReservationStatus.RESERVED, ReservationStatus.FINISHED, ReservationStatus.CONFIRMED)
                        }.map { it.slot },
                ),
        )
    }

    fun getPopularDesigner(): PopularDesignerResponse =
        redissonClient.getBucket<PopularDesignerResponse>(JobScheduler.POPULAR_DESIGNER_DATA_CACHE_KEY).get()
            ?: jobScheduler.getPopularDesigner() ?: throw TemporallyUnavailableException()
}
