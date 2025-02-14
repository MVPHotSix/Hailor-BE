package kr.hailor.hailor.controller.forUser.designer

import kr.hailor.hailor.service.DesignerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/designer")
class DesignerController(
    private val designerService: DesignerService,
) {
    @GetMapping
    fun searchDesigner(request: DesignerSearchRequest): DesignerSearchResponse = designerService.searchDesigner(request)

    @GetMapping("/regions")
    fun getRegions(): List<DesignerRegionResponse> = designerService.getRegions()
}
