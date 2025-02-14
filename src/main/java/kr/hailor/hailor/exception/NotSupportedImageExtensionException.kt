package kr.hailor.hailor.exception

class NotSupportedImageExtensionException :
    BaseException(
        ErrorCode.NOT_SUPPORTED_IMAGE_EXTENSION,
        ErrorCode.NOT_SUPPORTED_IMAGE_EXTENSION.errorMessage,
    )
