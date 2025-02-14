package kr.hailor.hailor.exception

class GoogleMeetLinkException :
    BaseException(
        ErrorCode.GOOGLE_MEET_LINK,
        ErrorCode.GOOGLE_MEET_LINK.errorMessage,
    )
