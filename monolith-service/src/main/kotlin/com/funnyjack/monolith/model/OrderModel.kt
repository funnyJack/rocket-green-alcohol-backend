package com.funnyjack.monolith.model

import com.funnyjack.monolith.entity.ContractType
import com.funnyjack.monolith.entity.Order

data class OrderCreateModel(
    val contractType: ContractType
)

data class OrderPatchModel(
    val contractType: ContractType? = null
)

data class OrderViewModel(
    val id: Long,
    val openid: String,
    val userNickname: String,
    val contractType: String,
    val contractedOwnerId: Long?,
    val contractedOwnerAmount: Int?,
    val greenAlcoholPioneerId: Long?,
    val greenAlcoholPioneerAmount: Int?,
    val greenAlcoholPartnersId: Long?,
    val greenAlcoholPartnersAmount: Int?
)

fun Order.toViewModel(): OrderViewModel {
    return OrderViewModel(
        id = id,
        openid = user!!.openid,
        userNickname = user!!.nickname ?: "",
        contractType = contractType.displayName,
        contractedOwnerId = contractedOwnerId,
        contractedOwnerAmount = contractedOwnerAmount,
        greenAlcoholPioneerId = greenAlcoholPioneerId,
        greenAlcoholPioneerAmount = greenAlcoholPioneerAmount,
        greenAlcoholPartnersId = greenAlcoholPartnersId,
        greenAlcoholPartnersAmount = greenAlcoholPartnersAmount
    )
}
