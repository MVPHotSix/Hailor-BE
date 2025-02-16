package kr.hailor.hailor.exception

class NeedGoogleAccessTokenException :
    BaseException(
        ErrorCode.NEED_GOOGLE_ACCESS_TOKEN,
        ErrorCode.NEED_GOOGLE_ACCESS_TOKEN.errorMessage,
    )
