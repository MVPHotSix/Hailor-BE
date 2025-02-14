package kr.hailor.hailor.controller.forAdmin.designer

import kr.hailor.hailor.service.DesignerService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/designer")
class AdminDesignerController(
    private val designerService: DesignerService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/region")
    fun createRegion(request: AdminDesignerRegionCreateRequest) {
        designerService.createRegion(request)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun createDesigner(
        @RequestPart("request")
        request: AdminDesignerCreateRequest,
        @RequestPart("profileImage") profileImage: MultipartFile,
    ) {
        designerService.createDesigner(request, profileImage)
    }

    @GetMapping
    fun searchDesigner(request: AdminDesignerSearchRequest): AdminDesignerSearchResponse = designerService.searchAdminDesigner(request)
}
