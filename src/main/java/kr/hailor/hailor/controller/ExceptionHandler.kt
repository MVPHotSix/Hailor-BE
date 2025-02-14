package kr.hailor.hailor.controller

import io.swagger.v3.oas.annotations.Hidden
import jakarta.validation.ConstraintViolationException
import kr.hailor.hailor.exception.BaseException
import kr.hailor.hailor.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.FileNotFoundException

@Hidden
@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalStateException::class,
        ConstraintViolationException::class,
    )
    fun invalidRequestException(ex: RuntimeException): ResponseEntity<ErrorResponse> =
        getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(FileNotFoundException::class)
    fun fileNotFoundException(ex: FileNotFoundException): ResponseEntity<ErrorResponse> =
        getErrorResponse(ErrorCode.FILE_NOT_FOUND, ex.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(BaseException::class)
    fun baseException(ex: BaseException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(ex.errorCode.httpStatus)
            .body(ErrorResponse.of(ex.errorCode, ex.message))

    private fun getErrorResponse(
        errorCode: ErrorCode,
        errorMessage: String?,
        httpStatus: HttpStatus,
    ): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.of(errorCode, errorMessage ?: "유효하지 않은 요청입니다"))
}

class ErrorResponse(
    val code: Int,
    val errorMessage: String,
) {
    companion object {
        fun of(
            errorCode: ErrorCode,
            errorMessage: String?,
        ) = ErrorResponse(errorCode.code, errorMessage ?: errorCode.errorMessage)
    }
}
