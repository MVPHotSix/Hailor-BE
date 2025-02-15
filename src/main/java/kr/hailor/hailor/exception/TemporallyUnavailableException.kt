package kr.hailor.hailor.exception

class TemporallyUnavailableException :
    BaseException(
        ErrorCode.TEMPORALLY_UNAVAILABLE,
        ErrorCode.TEMPORALLY_UNAVAILABLE.errorMessage,
    )
