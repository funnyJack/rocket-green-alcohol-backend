package com.funnyjack.monolith.model

import com.funnyjack.monolith.entity.ContractType

data class OrderCreateModel(
    val contractType: ContractType
)

data class OrderPatchModel(
    val contractType: ContractType? = null
)

data class OrderViewModel(
    val id: Long,
    val openid: String,
    val contractType: ContractType
)

fun com.funnyjack.monolith.entity.Order.toViewModel(): OrderViewModel {
    return OrderViewModel(
        id = id,
        openid = user!!.openid,
        contractType = contractType
    )
}
