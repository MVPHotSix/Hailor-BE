package kr.hailor.hailor.service

import kr.hailor.hailor.controller.forAdmin.terms.AdminTermsCreateRequest
import kr.hailor.hailor.controller.forUser.terms.TermInfoDTO
import kr.hailor.hailor.controller.forUser.terms.TermsResponse
import kr.hailor.hailor.enity.Terms
import kr.hailor.hailor.repository.ObjectStorageRepository
import kr.hailor.hailor.repository.TermsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TermsService(
    private val termsRepository: TermsRepository,
    private val objectStorageRepository: ObjectStorageRepository,
) {
    @Transactional(readOnly = true)
    fun getTerms(): TermsResponse {
        val terms = termsRepository.findAll()
        return TermsResponse(
            terms.map { TermInfoDTO(it.id, it.title, it.isRequired, objectStorageRepository.downloadUrl(it.contentFileName)) },
        )
    }

    fun createTerms(request: AdminTermsCreateRequest) {
        termsRepository.save(Terms(title = request.title, isRequired = request.isRequired, contentFileName = request.contentFileName))
    }
}
