package kr.hailor.hailor.exception

class InvalidMeetingTypeException :
    BaseException(
        ErrorCode.MEETING_TYPE_INVALID,
        ErrorCode.MEETING_TYPE_INVALID.errorMessage,
    )
