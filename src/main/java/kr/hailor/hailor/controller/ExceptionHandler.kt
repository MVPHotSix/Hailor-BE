package kr.hailor.hailor.controller

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.swagger.v3.oas.annotations.Hidden
import jakarta.validation.ConstraintViolationException
import kr.hailor.hailor.exception.BaseException
import kr.hailor.hailor.exception.ErrorCode
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.FileNotFoundException

@Hidden
@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val errorMessage =
            when (val cause = ex.cause) {
                is InvalidFormatException -> "${cause.path.joinToString(separator = ".") { it?.fieldName.orEmpty() }}: ${ex.message}"
                is MismatchedInputException -> {
                    "${cause.path.joinToString(separator = ".") { it?.fieldName.orEmpty() }}: ${ex.message}"
                }
                else -> "유효하지 않은 요청입니다"
            }
        return getErrorResponse(ErrorCode.INVALID_REQUEST, errorMessage, HttpStatus.BAD_REQUEST)
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    override fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.UNSUPPORTED_MEDIA_TYPE)

    override fun handleHandlerMethodValidationException(
        ex: HandlerMethodValidationException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalStateException::class,
        ConstraintViolationException::class,
    )
    fun invalidRequestException(ex: RuntimeException): ResponseEntity<Any> =
        getErrorResponse(ErrorCode.INVALID_REQUEST, ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(FileNotFoundException::class)
    fun fileNotFoundException(ex: FileNotFoundException): ResponseEntity<Any> =
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
    ): ResponseEntity<Any> =
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
