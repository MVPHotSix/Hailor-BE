package kr.hailor.hailor.controller.forUser.designer

import jakarta.validation.constraints.FutureOrPresent
import kr.hailor.hailor.service.DesignerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/designer")
class DesignerController(
    private val designerService: DesignerService,
) {
    @GetMapping
    fun searchDesigner(request: DesignerSearchRequest): DesignerSearchResponse = designerService.searchDesigner(request)

    @GetMapping("/regions")
    fun getRegions(): List<DesignerRegionResponse> = designerService.getRegions()

    @GetMapping("/{id}/schedule")
    fun getDesignerSchedule(
        @PathVariable id: Long,
        @FutureOrPresent
        date: LocalDate,
    ): DesignerScheduleResponse = designerService.getDesignerSchedule(id, date)
}
