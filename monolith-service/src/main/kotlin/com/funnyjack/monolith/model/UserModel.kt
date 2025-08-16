package com.funnyjack.monolith.model

import com.funnyjack.monolith.domain.SearchFilterCombineOperation
import com.funnyjack.monolith.dsl.combineSpecification
import com.funnyjack.monolith.dsl.equal
import com.funnyjack.monolith.dsl.like
import com.funnyjack.monolith.entity.User
import com.funnyjack.monolith.util.fullTrim
import org.springframework.data.jpa.domain.Specification

data class UserSearchFilter(
    val nicknameLike: String? = null,
    val phoneNumberLike: String? = null,
    val isSuperAdmin: Boolean? = null,
) {
    fun toSpecification(
        searchFilterOperation: SearchFilterCombineOperation
    ): Specification<User> {
        val nicknameLike = nicknameLike?.fullTrim()?.ifBlank { null }
        val phoneNumberLike = phoneNumberLike?.fullTrim()?.ifBlank { null }
        return combineSpecification(
            listOf(
                nicknameLike?.let { User::nickname.like("%$it%") },
                phoneNumberLike?.let { User::phoneNumber.like("%$it%") },
                isSuperAdmin?.let { User::superAdmin.equal(it) }
            ),
            searchFilterOperation.toOperation()
        )
    }
}

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
        isSuperAdmin = superAdmin,
        openid = openid,
        avatarUrl = avatarUrl,
        nickname = nickname,
        phoneNumber = phoneNumber,
        address = address,
        currentContractType = currentContractType?.displayName
    )
}
