package kr.hailor.hailor.service

import kr.hailor.hailor.config.properties.HostProperties
import kr.hailor.hailor.controller.forUser.reservation.ReservationCreateRequest
import kr.hailor.hailor.controller.forUser.reservation.ReservationInfoDto
import kr.hailor.hailor.controller.forUser.reservation.UserReservationsResponse
import kr.hailor.hailor.controller.forUser.reservation.UserReservationsSearchRequest
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.Reservation
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.exception.AlreadyReservedDesignerSlotException
import kr.hailor.hailor.exception.DesignerNotFoundException
import kr.hailor.hailor.exception.InvalidMeetingTypeException
import kr.hailor.hailor.exception.InvalidReservationDateException
import kr.hailor.hailor.repository.DesignerRepository
import kr.hailor.hailor.repository.ReservationRepository
import kr.hailor.hailor.util.LockUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val designerRepository: DesignerRepository,
    private val lockUtil: LockUtil,
    private val hostProperties: HostProperties,
) {
    @Transactional
    fun createReservation(
        user: User,
        request: ReservationCreateRequest,
    ) {
        val designer = designerRepository.findById(request.designerId).orElseThrow { DesignerNotFoundException() }
        val lockKey = lockUtil.getReservationLockKey(request.designerId, request.reservationDate)
        lockUtil.lock(
            lockName = lockKey,
            waitTime = 3,
            leaseTime = 3,
            timeUnit = TimeUnit.SECONDS,
        )
        if (reservationRepository.existsByDesignerIdAndReservationDateAndSlot(request.designerId, request.reservationDate, request.slot)) {
            lockUtil.unlock(lockKey)
            throw AlreadyReservedDesignerSlotException()
        }
        if ((designer.meetingType != MeetingType.OFFLINE_AND_ONLINE && designer.meetingType != request.meetingType) ||
            request.meetingType == MeetingType.OFFLINE_AND_ONLINE
        ) {
            lockUtil.unlock(lockKey)
            throw InvalidMeetingTypeException()
        }
        if (request.reservationDate.isBefore(LocalDate.now()) ||
            request.slot !in 0 until 20 ||
            (
                request.reservationDate.isEqual(LocalDate.now()) &&
                    LocalDateTime.now() >
                    LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(10 + request.slot / 2, if (request.slot % 2 == 0)0 else 30),
                    )
            )
        ) {
            lockUtil.unlock(lockKey)
            throw InvalidReservationDateException()
        }
        reservationRepository.save(
            Reservation(
                user = user,
                designer = designer,
                reservationDate = request.reservationDate,
                slot = request.slot,
                meetingType = designer.meetingType,
                status = ReservationStatus.RESERVED,
                paymentMethod = request.paymentMethod,
                price = if (request.meetingType == MeetingType.ONLINE) designer.onlinePrice else designer.offlinePrice,
            ),
        )
        lockUtil.unlock(lockKey)
    }

    @Transactional(readOnly = true)
    fun getReservations(
        user: User,
        request: UserReservationsSearchRequest,
    ): UserReservationsResponse {
        val reservations =
            if (request.lastId == null) {
                reservationRepository.findAllByUserOrderByIdDesc(user, Pageable.ofSize(request.size))
            } else {
                reservationRepository.findAllByIdLessThanAndUserOrderByIdDesc(request.lastId, user, Pageable.ofSize(request.size))
            }
        return UserReservationsResponse(
            reservations =
                reservations.content.map {
                    ReservationInfoDto(
                        id = it.id,
                        date = it.reservationDate,
                        status = it.status,
                        slot = it.slot,
                        meetingType = it.meetingType,
                        paymentMethod = it.paymentMethod,
                        googleMeetLink = it.googleMeetLink,
                        price = it.price,
                        designer =
                            ReservationInfoDto.ReservationDesignerInfoDto.of(
                                it.designer,
                                "${hostProperties.cdn}/${it.designer.profileImageName}",
                            ),
                    )
                },
        )
    }
}
