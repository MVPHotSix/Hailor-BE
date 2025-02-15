package kr.hailor.hailor.exception

class PaymentTypeMismatchException :
    BaseException(
        ErrorCode.PAYMENT_TYPE_MISMATCH,
        ErrorCode.PAYMENT_TYPE_MISMATCH.errorMessage,
    )
