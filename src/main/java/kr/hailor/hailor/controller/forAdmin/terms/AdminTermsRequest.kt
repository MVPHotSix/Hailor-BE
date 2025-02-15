package kr.hailor.hailor.controller.forAdmin.terms

data class AdminTermsCreateRequest(
    val title: String,
    val isRequired: Boolean,
    val contentFileName: String,
)
