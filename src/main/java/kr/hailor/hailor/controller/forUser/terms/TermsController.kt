package kr.hailor.hailor.controller.forUser.terms

import kr.hailor.hailor.service.TermsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/terms")
class TermsController(
    private val termsService: TermsService,
) {
    @GetMapping
    fun getTerms(): TermsResponse = termsService.getTerms()
}
