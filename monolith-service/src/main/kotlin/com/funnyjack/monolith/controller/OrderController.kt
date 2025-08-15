package com.funnyjack.monolith.controller

import com.funnyjack.monolith.annotation.CurrentUserOpenId
import com.funnyjack.monolith.model.*
import com.funnyjack.monolith.service.OrderService
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Transactional(rollbackFor = [Throwable::class])
@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    fun createOrder(
        @RequestBody orderCreateModel: OrderCreateModel,
        @CurrentUserOpenId openid: String
    ): OrderViewModel = orderService.createOrder(openid,orderCreateModel).toViewModel()

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): OrderViewModel =
        orderService.getOrderById(id).toViewModel()

    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody patchModel: OrderPatchModel
    ): OrderViewModel = orderService.updateOrder(id, patchModel).toViewModel()

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long) {
        orderService.deleteOrder(id)
    }
}