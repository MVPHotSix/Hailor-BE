package kr.hailor.hailor.controller.forAdmin.reservation

import kr.hailor.hailor.service.PaymentService
import kr.hailor.hailor.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/reservation")
class AdminReservationController(
    private val paymentService: PaymentService,
    private val reservationService: ReservationService,
) {
    @PostMapping("/{id}/confirm")
    fun confirmPayment(
        @PathVariable id: Long,
    ) {
        paymentService.confirmPayment(id)
    }

    @GetMapping
    fun getReservations(request: AdminReservationSearchRequest): AdminReservationListResponse =
        reservationService.getAdminReservations(request)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{id}/refund")
    fun refundReservation(
        @PathVariable id: Long,
    ) {
        reservationService.refundReservation(id)
    }
}
