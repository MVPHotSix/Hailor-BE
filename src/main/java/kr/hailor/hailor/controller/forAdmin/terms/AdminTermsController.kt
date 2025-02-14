package kr.hailor.hailor.controller.forAdmin.terms

import kr.hailor.hailor.service.TermsService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/terms")
class AdminTermsController(
    private val termsService: TermsService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createTerms(request: AdminTermsRequest) {
        termsService.createTerms(request)
    }
}
