package kr.hailor.hailor.controller

import kr.hailor.hailor.repository.ObjectStorageRepository
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

// TODO 이 컨트롤러는 S3 테스트 용이므로 나중에 다들 사용법을 숙지한 이후에는 꼭 지울것
@RequestMapping("/api/v1/files")
@RestController
class FileTestController(
    private val objectStorageRepository: ObjectStorageRepository,
) {
    @ResponseStatus(CREATED)
    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(
        @RequestPart
        multipartFile: MultipartFile,
    ) {
        objectStorageRepository.upload(
            UUID.randomUUID().toString() + '.' + multipartFile.originalFilename!!.split(".")[1],
            multipartFile.inputStream,
        )
    }

    @GetMapping
    fun getFileUrl(fileName: String): String = objectStorageRepository.downloadUrl(fileName)
}
