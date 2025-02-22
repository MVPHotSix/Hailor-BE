package kr.hailor.hailor.service

import kr.hailor.hailor.controller.forUser.meeting.GoogleMeetCreateRequest
import kr.hailor.hailor.enity.MeetingType
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.exception.InvalidMeetingTypeException
import kr.hailor.hailor.exception.NotConfirmedException
import kr.hailor.hailor.exception.ReservationNotFoundException
import kr.hailor.hailor.repository.ReservationRepository
import kr.hailor.hailor.util.GoogleMeetManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MeetService(
    private val googleMeetManager: GoogleMeetManager,
    private val reservationRepository: ReservationRepository,
) {
    @Transactional
    fun createGoogleMeetLink(
        user: User,
        request: GoogleMeetCreateRequest,
    ): String {
        val reservation = reservationRepository.findById(request.reservationId).orElseThrow { ReservationNotFoundException() }

        if (reservation.user.id != user.id) {
            throw ReservationNotFoundException()
        }
        if (reservation.status != ReservationStatus.CONFIRMED) {
            throw NotConfirmedException()
        }
        if (reservation.meetingType == MeetingType.OFFLINE) {
            throw InvalidMeetingTypeException()
        }

        val googleMeetCreateResult = googleMeetManager.createGoogleMeet(reservation, request.googleAccessToken)
        reservation.googleCalendarEventId = googleMeetCreateResult.first
        reservation.googleMeetLink = googleMeetCreateResult.second
        return googleMeetCreateResult.second
    }
}
