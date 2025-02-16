package kr.hailor.hailor.service

import kr.hailor.hailor.controller.forUser.meeting.GoogleMeetCreateRequest
import kr.hailor.hailor.enity.ReservationStatus
import kr.hailor.hailor.exception.NotConfirmedException
import kr.hailor.hailor.exception.ReservationNotFoundException
import kr.hailor.hailor.repository.ReservationRepository
import kr.hailor.hailor.util.GoogleMeetCreator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MeetService(
    private val googleMeetCreator: GoogleMeetCreator,
    private val reservationRepository: ReservationRepository,
) {
    @Transactional
    fun createGoogleMeetLink(request: GoogleMeetCreateRequest): String {
        val reservation = reservationRepository.findById(request.reservationId).orElseThrow { ReservationNotFoundException() }

        if (reservation.status != ReservationStatus.CONFIRMED) {
            throw NotConfirmedException()
        }

        return googleMeetCreator.createGoogleMeet(reservation, request.googleAuthCode)
    }
}
