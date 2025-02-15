package kr.hailor.hailor.exception

class AlreadyDoneException :
    BaseException(
        ErrorCode.ALREADY_DONE,
        ErrorCode.ALREADY_DONE.errorMessage,
    )
