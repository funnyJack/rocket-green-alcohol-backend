package com.funnyjack.monolith.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Transactional(rollbackFor = [Throwable::class])
@RestController
@RequestMapping("/userInfos")
@CrossOrigin
class UserInfoController {
    @GetMapping("/hello")
    fun login(): String{
        return "hello,world"
    }
}
