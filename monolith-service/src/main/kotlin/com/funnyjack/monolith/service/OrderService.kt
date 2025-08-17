package com.funnyjack.monolith.service

import com.funnyjack.monolith.entity.Order
import com.funnyjack.monolith.entity.OrderRepository
import com.funnyjack.monolith.entity.UserRepository
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