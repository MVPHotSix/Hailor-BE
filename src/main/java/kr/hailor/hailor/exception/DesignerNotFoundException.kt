package kr.hailor.hailor.exception

class DesignerNotFoundException :
    BaseException(
        ErrorCode.DESIGNER_NOT_FOUND,
        ErrorCode.DESIGNER_NOT_FOUND.errorMessage,
    )
