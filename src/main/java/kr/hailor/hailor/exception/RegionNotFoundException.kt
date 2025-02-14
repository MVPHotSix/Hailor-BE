package kr.hailor.hailor.exception

class RegionNotFoundException :
    BaseException(
        ErrorCode.REGION_NOT_FOUND,
        ErrorCode.REGION_NOT_FOUND.errorMessage,
    )
