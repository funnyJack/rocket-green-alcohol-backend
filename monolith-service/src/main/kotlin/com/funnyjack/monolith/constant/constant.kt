package com.funnyjack.monolith.constant

import com.funnyjack.monolith.entity.ContractType

object LoginConstant {
    const val LOGIN_PATH = "https://api.weixin.qq.com/sns/jscode2session"
    const val AUTHORIZATION_CODE = "rocket-green-alcohol"
    const val APPID = "wx6d5a94f107744172"
    const val SECRET = "2b5fa18596cc394b98c279032c551a4f"
}

object OrderConstant {
    // Order 的类型 to user 的类型，后面是 user 应该获取的利润
    val GROSS_PROFIT_MAP = mapOf(
        Pair(ContractType.CONTRACTED_OWNER, ContractType.CONTRACTED_OWNER) to 100,
        Pair(ContractType.CONTRACTED_OWNER, ContractType.GREEN_ALCOHOL_PIONEER) to 150,
        Pair(ContractType.CONTRACTED_OWNER, ContractType.GREEN_ALCOHOL_PARTNERS) to 200,

        Pair(ContractType.GREEN_ALCOHOL_PIONEER, ContractType.CONTRACTED_OWNER) to 100,
        Pair(ContractType.GREEN_ALCOHOL_PIONEER, ContractType.GREEN_ALCOHOL_PIONEER) to 200,
        Pair(ContractType.GREEN_ALCOHOL_PIONEER, ContractType.GREEN_ALCOHOL_PARTNERS) to 300,

        Pair(ContractType.GREEN_ALCOHOL_PARTNERS, ContractType.CONTRACTED_OWNER) to 100,
        Pair(ContractType.GREEN_ALCOHOL_PARTNERS, ContractType.GREEN_ALCOHOL_PIONEER) to 500,
        Pair(ContractType.GREEN_ALCOHOL_PARTNERS, ContractType.GREEN_ALCOHOL_PARTNERS) to 1500,
    )

    fun getUserGrossProfit(orderContractType: ContractType, userContractType: ContractType): Int {
        return GROSS_PROFIT_MAP.getOrElse(Pair(orderContractType, userContractType)) {
            throw IllegalArgumentException("No value defined for ${orderContractType.displayName} and ${userContractType.displayName}")
        }
    }
}