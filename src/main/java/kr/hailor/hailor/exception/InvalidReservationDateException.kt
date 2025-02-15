package kr.hailor.hailor.exception

class InvalidReservationDateException :
    BaseException(
        ErrorCode.INVALID_RESERVATION_DATE,
        ErrorCode.INVALID_RESERVATION_DATE.errorMessage,
    )
