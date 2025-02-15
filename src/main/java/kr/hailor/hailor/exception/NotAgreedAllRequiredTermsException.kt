package kr.hailor.hailor.exception

class NotAgreedAllRequiredTermsException :
    BaseException(
        ErrorCode.NOT_AGREED_ALL_REQUIRED_TERMS,
        ErrorCode.NOT_AGREED_ALL_REQUIRED_TERMS.errorMessage,
    )
