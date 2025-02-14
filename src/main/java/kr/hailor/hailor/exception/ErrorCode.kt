package kr.hailor.hailor.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val errorMessage: String,
) {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, -40000, "유효하지 않은 요청입니다"),
    NOT_AGREED_ALL_REQUIRED_TERMS(HttpStatus.BAD_REQUEST, -40001, "모든 필수 약관에 동의해야 합니다"),

    AUTHENTICATED_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, -40100, "인증된 사용자를 찾을 수 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, -40400, "찾을 수 없습니다"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, -40401, "파일을 찾을 수 없습니다"),

    USER_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, -40300, "등록되지 않은 사용자입니다"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -50000, "서버 내부 오류입니다"),
    FAIL_TO_REQUEST_KAKAO_PAY_API(HttpStatus.INTERNAL_SERVER_ERROR, -50001, "카카오페이 준비 요청 실패"),
}
