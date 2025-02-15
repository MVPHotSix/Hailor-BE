package kr.hailor.hailor.exception

class NotConfirmedException :
    BaseException(
        ErrorCode.NOT_CONFIRMED,
        ErrorCode.NOT_CONFIRMED.errorMessage,
    )
