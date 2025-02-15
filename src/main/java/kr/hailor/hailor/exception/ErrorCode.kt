package kr.hailor.hailor.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val errorMessage: String,
) {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, -40000, "유효하지 않은 요청입니다"),
    NOT_AGREED_ALL_REQUIRED_TERMS(HttpStatus.BAD_REQUEST, -40001, "모든 필수 약관에 동의해야 합니다"),
    NOT_SUPPORTED_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, -40002, "지원하지 않는 이미지 확장자입니다"),
    ALREADY_RESERVED_DESIGNER_SLOT(HttpStatus.BAD_REQUEST, -40003, "이미 예약된 디자이너 슬롯입니다"),
    MEETING_TYPE_INVALID(HttpStatus.BAD_REQUEST, -40004, "미팅 타입이 유효하지 않습니다"),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, -40005, "유효하지 않은 예약 날짜입니다"),
    ALREADY_PAID(HttpStatus.BAD_REQUEST, -40006, "이미 결제된 예약입니다"),
    PAYMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, -40007, "결제 타입이 일치하지 않습니다"),
    NOT_CONFIRMED(HttpStatus.BAD_REQUEST, -40008, "예약금이 미입금된 예약입니다"),
    ALREADY_DONE(HttpStatus.BAD_REQUEST, -40009, "이미 처리 완료된 예약입니다"),

    AUTHENTICATED_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, -40100, "인증된 사용자를 찾을 수 없습니다."),

    USER_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, -40300, "등록되지 않은 사용자입니다"),

    NOT_FOUND(HttpStatus.NOT_FOUND, -40400, "찾을 수 없습니다"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, -40401, "파일을 찾을 수 없습니다"),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, -40402, "지역을 찾을 수 없습니다"),
    DESIGNER_NOT_FOUND(HttpStatus.NOT_FOUND, -40403, "디자이너를 찾을 수 없습니다"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, -40404, "예약을 찾을 수 없습니다"),

    ALREADY_REGISTERED_USER(HttpStatus.CONFLICT, -40900, "이미 가입된 사용자입니다"),

    TEMPORALLY_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, -50000, "일시적인 오류입니다. 잠시 후 다시 시도해주세요"),
    FAIL_TO_REQUEST_KAKAO_PAY_API(HttpStatus.INTERNAL_SERVER_ERROR, -50001, "카카오페이 준비 요청 실패"),
    GOOGLE_MEET_LINK(HttpStatus.INTERNAL_SERVER_ERROR, -50002, "Google Meet 링크 생성 실패"),
}
