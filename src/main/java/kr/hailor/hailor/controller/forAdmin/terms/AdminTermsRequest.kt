package kr.hailor.hailor.controller.forAdmin.terms

data class AdminTermsRequest(
    val title: String,
    val isRequired: Boolean,
    val contentFileName: String,
)
