package kr.hailor.hailor.controller.forUser.terms

data class TermsResponse(
    val terms: List<TermInfoDTO>,
)

data class TermInfoDTO(
    val id: Long,
    val title: String,
    val isRequired: Boolean,
    val contentUrl: String,
)
