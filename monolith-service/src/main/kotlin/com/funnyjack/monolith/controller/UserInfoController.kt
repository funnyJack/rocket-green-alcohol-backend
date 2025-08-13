package com.funnyjack.monolith.controller

import com.funnyjack.monolith.model.LoginRequestModel
import com.funnyjack.monolith.service.UserInfoService
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Transactional(rollbackFor = [Throwable::class])
@RestController
@RequestMapping("/userInfos")
@CrossOrigin
class UserInfoController(
    private val userInfoService: UserInfoService
) {
    @PostMapping("/login")
    fun login(
       @RequestBody loginModel: LoginRequestModel
    ): String{
        return userInfoService.login(loginModel.jsCode)
    }
}
