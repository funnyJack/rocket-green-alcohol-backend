package com.funnyjack.monolith.model

data class LoginRequestModel(
    val jsCode: String,
)

data class LoginResponseModel(
    val openid: String?,
    val session_key: String?,
    val unionid: String?,
    val errcode: String?,
    val errmsg: String?,
)
