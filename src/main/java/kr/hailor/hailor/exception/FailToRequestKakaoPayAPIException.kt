package kr.hailor.hailor.exception

class FailToRequestKakaoPayAPIException(
    desc: String,
) : BaseException(
        ErrorCode.FAIL_TO_REQUEST_KAKAO_PAY_API,
        "카카오페이 준비 요청 실패: $desc",
    )
