package com.funnyjack.monolith.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.funnyjack.monolith.constant.LoginConstant
import com.funnyjack.monolith.dsl.equal
import com.funnyjack.monolith.dsl.or
import com.funnyjack.monolith.entity.Order
import com.funnyjack.monolith.entity.OrderRepository
import com.funnyjack.monolith.entity.User
import com.funnyjack.monolith.entity.UserRepository
import com.funnyjack.monolith.model.LoginResponseModel
import com.funnyjack.monolith.model.UserCreateModel
import com.funnyjack.monolith.model.UserPatchModel
import com.funnyjack.monolith.util.JwtUtil
import com.hiczp.spring.error.BadRequestException
import com.hiczp.spring.error.NotFoundException
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class UserService(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) {
    fun search(specification: Specification<User>): List<User> = userRepository.findAll(specification)

    fun checkIsSuperAdmin(openid: String) = userRepository.existsByOpenidAndSuperAdminTrue(openid)

    fun createUser(userCreateModel: UserCreateModel): User {
        // Check if user with this openid already exists
        if (userRepository.existsByOpenid(userCreateModel.openid)) {
            throw BadRequestException("User with this openid already exists")
        }
        var referralCode: String
        //确保推荐码不重复
        do {
            referralCode = User.generateReferralCode()
        } while (userRepository.existsByReferralCode(referralCode))

        val user = User(
            openid = userCreateModel.openid,
            avatarUrl = userCreateModel.avatarUrl,
            nickname = userCreateModel.nickname,
            phoneNumber = userCreateModel.phoneNumber,
            address = userCreateModel.address,
            referralCode = referralCode
        )

        return userRepository.save(user)
    }

    fun getUser(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            throw NotFoundException("User not found")
        }
    }

    fun getUserGrossProfit(openid: String): Int {
        val user = getUserByOpenid(openid)
        val contractedOwnerAmount =
            orderRepository.findByContractedOwnerId(user.id).sumOf { order -> order.contractedOwnerAmount ?: 0 }
        val greenAlcoholPioneerAmount =
            orderRepository.findByGreenAlcoholPioneerId(user.id).sumOf { order -> order.greenAlcoholPioneerAmount ?: 0 }
        val greenAlcoholPartnersAmount = orderRepository.findByGreenAlcoholPartnersId(user.id)
            .sumOf { order -> order.greenAlcoholPartnersAmount ?: 0 }
        return contractedOwnerAmount + greenAlcoholPioneerAmount + greenAlcoholPartnersAmount
    }

    fun getUserGrossProfitDetail(openid: String): List<Order> {
        val user = getUserByOpenid(openid)
        val specification = or(
            Order::contractedOwnerId.equal(user.id.toInt()),
            Order::greenAlcoholPioneerId.equal(user.id.toInt()),
            Order::greenAlcoholPartnersId.equal(user.id.toInt())
        )
        return orderRepository.findAll(specification)
    }

    fun getUserByOpenid(openid: String): User {
        return userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")
    }

    fun updateUser(openid: String, patchModel: UserPatchModel): User {
        val user = userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")
        patchModel.referrerCode?.let {
            val referrer = userRepository.findByReferralCode(it) ?: throw BadRequestException("推荐码无效")
            if (user.referrer != null && user.referrer != referrer) {
                throw BadRequestException("你已经有推荐人了，不能修改推荐码")
            }
            if (referrer.id == user.id) {
                throw BadRequestException("不能推荐自己")
            }
            if (referrer.superAdmin) {
                throw BadRequestException("管理员不能推荐用户")
            }
            user.referrer = referrer
        }
        patchModel.avatarUrl?.let { user.avatarUrl = it }
        patchModel.nickname?.let { user.nickname = it }
        patchModel.phoneNumber?.let { user.phoneNumber = it }
        patchModel.address?.let { user.address = it }

        return userRepository.save(user)
    }

    fun deleteUserByOpenid(openid: String) {
        val user = userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")
        userRepository.delete(user)
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id).orElseThrow {
            throw NotFoundException("User not found")
        }
        userRepository.delete(user)
    }

    fun login(jsCode: String): String {
        val restTemplate = RestTemplate()
        val uri = UriComponentsBuilder.fromUriString(LoginConstant.LOGIN_PATH).apply {
            queryParam("appid", LoginConstant.APPID)
            queryParam("secret", LoginConstant.SECRET)
            queryParam("grant_type", LoginConstant.AUTHORIZATION_CODE)
            queryParam("js_code", jsCode)
        }.toUriString()
        val result = restTemplate.getForObject(uri, String::class.java).let {
            objectMapper.readValue(it, LoginResponseModel::class.java)
        }
        val openid = result.openid
        if (openid == null) {
            throw BadRequestException("login failed, error code: ${result.errcode}, error message: ${result.errmsg}")
        }
        //保存当前用户
        if (!userRepository.existsByOpenid(openid)) {
            createUser(UserCreateModel(openid = openid))
        }
        // 生成 JWT token
        return JwtUtil.generateToken(openid)
    }
}
