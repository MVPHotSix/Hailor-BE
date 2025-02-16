package kr.hailor.hailor.controller.forUser.reservation

import io.swagger.v3.oas.annotations.Parameter
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reservation")
class ReservationController(
    private val reservationService: ReservationService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createReservation(
        @Parameter(hidden = true)
        user: User,
        @RequestBody
        request: ReservationCreateRequest,
    ) = reservationService.createReservation(user, request)

    @GetMapping
    fun getReservations(
        @Parameter(hidden = true)
        user: User,
        request: UserReservationsSearchRequest,
    ): UserReservationsResponse = reservationService.getReservations(user, request)

    @GetMapping("/recently-finished")
    fun getRecentFinishedReservation(
        @Parameter(hidden = true)
        user: User,
        request: UserReservationsSearchRequest,
    ): UserReservationsResponse = reservationService.getRecentFinishedReservation(user, request)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun cancelReservation(
        @Parameter(hidden = true)
        user: User,
        @Parameter(description = "예약 ID")
        id: Long,
        @RequestBody
        request: ReservationCancelRequest,
    ) = reservationService.cancelReservation(user, id, request)
}
