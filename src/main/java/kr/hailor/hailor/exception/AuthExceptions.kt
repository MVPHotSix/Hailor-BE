package kr.hailor.hailor.exception

class AuthenticatedUserNotFoundException :
    BaseException(
        ErrorCode.AUTHENTICATED_USER_NOT_FOUND,
        ErrorCode.AUTHENTICATED_USER_NOT_FOUND.errorMessage,
    )
