package com.funnyjack.monolith.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.funnyjack.monolith.constant.LoginConstant
import com.funnyjack.monolith.entity.User
import com.funnyjack.monolith.entity.UserRepository
import com.funnyjack.monolith.model.LoginResponseModel
import com.funnyjack.monolith.model.UserCreateModel
import com.funnyjack.monolith.model.UserPatchModel
import com.funnyjack.monolith.util.JwtUtil
import com.hiczp.spring.error.BadRequestException
import com.hiczp.spring.error.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class UserService(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository
) {
    fun createUser(userCreateModel: UserCreateModel): User {
        // Check if user with this openid already exists
        if (userRepository.findByOpenid(userCreateModel.openid) != null) {
            throw BadRequestException("User with this openid already exists")
        }

        val user = User(
            openid = userCreateModel.openid,
            avatarUrl = userCreateModel.avatarUrl,
            nickname = userCreateModel.nickname,
            phoneNumber = userCreateModel.phoneNumber,
            address = userCreateModel.address
        )

        return userRepository.save(user)
    }

    fun getUserByOpenid(openid: String): User {
        return userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")
    }

    fun updateUser(openid: String, patchModel: UserPatchModel): User {
        val user = userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")

        patchModel.avatarUrl?.let { user.avatarUrl = it }
        patchModel.nickname?.let { user.nickname = it }
        patchModel.phoneNumber?.let { user.phoneNumber = it }
        patchModel.address?.let { user.address = it }

        return userRepository.save(user)
    }

    fun deleteUser(openid: String) {
        val user = userRepository.findByOpenid(openid) ?: throw NotFoundException("User not found")
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
        if (userRepository.findByOpenid(openid) == null) {
            createUser(UserCreateModel(openid = openid))
        }
        // 生成 JWT token
        return JwtUtil.generateToken(openid)
    }
}