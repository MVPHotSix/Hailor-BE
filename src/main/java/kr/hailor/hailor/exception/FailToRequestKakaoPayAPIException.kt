package kr.hailor.hailor.exception

class FailToRequestKakaoPayAPIException(
    desc: String,
) : RuntimeException("카카오페이 준비 요청 실패: $desc")
