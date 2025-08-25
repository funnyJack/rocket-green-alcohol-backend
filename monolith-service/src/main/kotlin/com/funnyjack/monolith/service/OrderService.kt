package com.funnyjack.monolith.service

import com.funnyjack.monolith.constant.OrderConstant
import com.funnyjack.monolith.entity.*
import com.funnyjack.monolith.model.OrderCreateModel
import com.funnyjack.monolith.model.OrderPatchModel
import com.hiczp.spring.error.BadRequestException
import com.hiczp.spring.error.NotFoundException
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) {
    fun createOrder(openid: String, orderCreateModel: OrderCreateModel): Order {
        // Check if user with this openid exists
        val user = userRepository.findByOpenid(openid)
            ?: throw BadRequestException("User with this openid does not exist")
        if (user.referrer == null) {
            throw BadRequestException("你还没有推荐人，不能创建订单")
        }
        val savedUser = user.apply {
            this.currentContractType = orderCreateModel.contractType
        }.let {
            userRepository.save(it)
        }
        val order = Order(
            user = savedUser,
            contractType = orderCreateModel.contractType
        )

        //处理利润分配的逻辑
        //针对不同的用户类型，填写 order 的不同字段
        fun modifyOrder(user: User, amount: Int) {
            when (user.currentContractType!!) {
                ContractType.CONTRACTED_OWNER -> {
                    order.contractedOwnerId = user.id
                    order.contractedOwnerAmount = amount
                }

                ContractType.GREEN_ALCOHOL_PIONEER -> {
                    order.greenAlcoholPioneerId = user.id
                    order.greenAlcoholPioneerAmount = amount
                }

                ContractType.GREEN_ALCOHOL_PARTNERS -> {
                    order.greenAlcoholPartnersId = user.id
                    order.greenAlcoholPartnersAmount = amount
                }
            }
        }
        // 拿出当前 contract type 的总利润
        var totalGrossProfit = orderCreateModel.contractType.totalGrossProfit
        //从当前用户的推荐人开始，逐级向上分配利润，直到利润分配完毕或者没有推荐人为止
        var currentUser: User = user
        var userGrossProfitTemp = 0
        while (totalGrossProfit > 0) {
            val referrer = currentUser.referrer!!
            //计算推荐人应得的利润
            val grossProfit = OrderConstant.getUserGrossProfit(
                orderCreateModel.contractType, referrer.currentContractType!!
            )
            //如果当前推荐人的利润与上一个获得利润的推荐人一样，则说明当前推荐人与上一个推荐人平级，不应该获得利润，继续向上寻找
            if (grossProfit == userGrossProfitTemp) {
                currentUser = referrer
                continue
            }
            //推荐人实际应得的利润
            val actualGrossProfit = grossProfit - userGrossProfitTemp
            //填写订单
            modifyOrder(referrer, actualGrossProfit)
            // 更新已分配的利润
            totalGrossProfit = totalGrossProfit - actualGrossProfit
            currentUser = referrer
            userGrossProfitTemp = grossProfit
        }
        return orderRepository.save(order)
    }

    fun getOrderById(id: Long): Order {
        return orderRepository.findById(id).orElseThrow { NotFoundException("Order not found") }
    }

    fun updateOrder(id: Long, patchModel: OrderPatchModel): Order {
        val order = orderRepository.findById(id).orElseThrow { NotFoundException("Order not found") }

        patchModel.contractType?.let { order.contractType = it }

        return orderRepository.save(order)
    }

    fun deleteOrder(id: Long) {
        val order = orderRepository.findById(id).orElseThrow { NotFoundException("Order not found") }
        orderRepository.delete(order)
    }
}