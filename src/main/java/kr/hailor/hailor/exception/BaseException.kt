package kr.hailor.hailor.exception

open class BaseException(
    val errorCode: ErrorCode,
    message: String?,
) : RuntimeException(message)
