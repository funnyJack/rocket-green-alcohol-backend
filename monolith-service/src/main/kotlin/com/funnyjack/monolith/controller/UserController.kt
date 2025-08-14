package com.funnyjack.monolith.controller

import com.funnyjack.monolith.annotation.CurrentUserOpenId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/me")
    fun getCurrentUser(@CurrentUserOpenId openid: String): Map<String, String> {
        return mapOf("openid" to openid)
    }
}
