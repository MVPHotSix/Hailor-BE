package kr.hailor.hailor.exception

class ReservationNotFoundException :
    BaseException(
        ErrorCode.RESERVATION_NOT_FOUND,
        ErrorCode.RESERVATION_NOT_FOUND.errorMessage,
    )
