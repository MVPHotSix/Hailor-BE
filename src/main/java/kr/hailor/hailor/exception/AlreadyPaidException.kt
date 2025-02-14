package kr.hailor.hailor.exception

class AlreadyPaidException :
    BaseException(
        ErrorCode.ALREADY_PAID,
        ErrorCode.ALREADY_PAID.errorMessage,
    )
