package com.funnyjack.monolith.model

import com.funnyjack.monolith.entity.User

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
    val isSuperAdmin: Boolean,
    val avatarUrl: String?,
    val nickname: String?,
    val phoneNumber: String?,
    val address: String?,
    val currentContractType: String? = null
)

fun User.toViewModel(): UserViewModel {
    return UserViewModel(
        id = id,
        isSuperAdmin = isSuperAdmin,
        openid = openid,
        avatarUrl = avatarUrl,
        nickname = nickname,
        phoneNumber = phoneNumber,
        address = address,
        currentContractType = currentContractType?.displayName
    )
}
