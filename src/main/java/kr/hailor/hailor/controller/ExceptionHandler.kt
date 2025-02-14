package kr.hailor.hailor.controller

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.FileNotFoundException

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalStateException::class,
        ConstraintViolationException::class,
    )
    fun invalidRequestException(ex: RuntimeException): ResponseEntity<ErrorResponse> = getErrorResponse(ex.message)

    @ExceptionHandler(FileNotFoundException::class)
    fun fileNotFoundException(ex: FileNotFoundException): ResponseEntity<ErrorResponse> = getErrorResponse(ex.message, HttpStatus.NOT_FOUND)

    private fun getErrorResponse(
        errorMessage: String?,
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    ): ResponseEntity<ErrorResponse> {
        val invalidRequestErrorCode = 400
        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse(invalidRequestErrorCode, errorMessage ?: "유효하지 않은 요청입니다"))
    }
}

class ErrorResponse(
    val code: Int,
    val errorMessage: String,
)
