package com.funnyjack.monolith.model

data class UserCreateModel(
    val openid: String,
    val avatarUrl: String? = null,
    val nickname: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null
)

data class UserPatchModel(
    val avatarUrl: String? = null,
    val nickname: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null
)

data class UserViewModel(
    val id: Long,
    val openid: String,
    val avatarUrl: String?,
    val nickname: String?,
    val phoneNumber: String?,
    val address: String?
)
