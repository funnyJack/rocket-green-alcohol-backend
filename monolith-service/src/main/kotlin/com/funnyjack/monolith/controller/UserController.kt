package com.funnyjack.monolith.controller

import com.funnyjack.monolith.annotation.CurrentUserOpenId
import com.funnyjack.monolith.entity.User
import com.funnyjack.monolith.model.LoginRequestModel
import com.funnyjack.monolith.model.UserCreateModel
import com.funnyjack.monolith.model.UserPatchModel
import com.funnyjack.monolith.model.UserViewModel
import com.funnyjack.monolith.model.toViewModel
import com.funnyjack.monolith.service.UserService
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Transactional(rollbackFor = [Throwable::class])
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody userCreateModel: UserCreateModel): UserViewModel =
        userService.createUser(userCreateModel).toViewModel()

    @GetMapping
    fun getCurrentUser(@CurrentUserOpenId openid: String): UserViewModel =
        userService.getUserByOpenid(openid).toViewModel()

    //管理员获取人员
    @GetMapping("/{openid}")
    fun getUserByOpenid(@PathVariable openid: String): UserViewModel =
        userService.getUserByOpenid(openid).toViewModel()

    @PutMapping
    fun updateCurrentUser(
        @CurrentUserOpenId openid: String,
        @RequestBody patchModel: UserPatchModel
    ): UserViewModel = userService.updateUser(openid, patchModel).toViewModel()

    //    管理员的相关操作
    @PutMapping("/{openid}")
    fun updateUserByOpenid(
        @PathVariable openid: String,
        @RequestBody patchModel: UserPatchModel
    ): UserViewModel = userService.updateUser(openid, patchModel).toViewModel()

    @DeleteMapping
    fun deleteCurrentUser(@CurrentUserOpenId openid: String) {
        userService.deleteUser(openid)
    }

    //可能管理员需要删除某个成员
    @DeleteMapping("/{openid}")
    fun deleteUserByOpenid(@PathVariable openid: String) {
        userService.deleteUser(openid)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginModel: LoginRequestModel
    ): String {
        return userService.login(loginModel.jsCode)
    }
}
