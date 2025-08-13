package com.funnyjack.monolith.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.funnyjack.monolith.constant.LoginConstant
import com.funnyjack.monolith.model.LoginResponseModel
import com.hiczp.spring.error.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class UserInfoService(
    private val objectMapper: ObjectMapper
) {
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
        if (result.openid == null) {
            throw BadRequestException("login failed, error code: ${result.errcode}, error message: ${result.errmsg}")
        }
        return result.openid
}
}
