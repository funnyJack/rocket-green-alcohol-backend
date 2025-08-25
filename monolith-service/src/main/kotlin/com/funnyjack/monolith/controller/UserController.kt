package com.funnyjack.monolith.controller

import com.funnyjack.monolith.annotation.CurrentUserOpenId
import com.funnyjack.monolith.domain.SearchFilterCombineOperation
import com.funnyjack.monolith.model.*
import com.funnyjack.monolith.service.UserService
import com.hiczp.spring.error.UnauthorizedException
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Transactional(rollbackFor = [Throwable::class])
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/search")
    fun search(
        @RequestBody filter: UserSearchFilter,
        @RequestParam(required = false, defaultValue = "OR")
        searchFilterCombineOperation: SearchFilterCombineOperation,
        @CurrentUserOpenId openid: String
    ): List<UserViewModel> {
        if (!userService.checkIsSuperAdmin(openid)) {
            throw UnauthorizedException("你没有权限进行查询")
        }
        return userService.search(
            filter.toSpecification(searchFilterCombineOperation)
        ).map { it.toViewModel() }
    }

    @PostMapping
    fun createUser(@RequestBody userCreateModel: UserCreateModel): UserViewModel =
        userService.createUser(userCreateModel).toViewModel()

    @GetMapping
    fun getCurrentUser(@CurrentUserOpenId openid: String): UserViewModel =
        userService.getUserByOpenid(openid).toViewModel()

    @GetMapping("/grossProfit")
    fun getGrossProfit(
        @CurrentUserOpenId openid: String
    ) = userService.getUserGrossProfit(openid)

    @GetMapping("/grossProfitDetail")
    fun getGrossProfitDetail(
        @CurrentUserOpenId openid: String
    ) = userService.getUserGrossProfitDetail(openid).map { it.toViewModel() }

    //管理员获取人员
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
        @CurrentUserOpenId openid: String
    ): UserViewModel {
        if (!userService.checkIsSuperAdmin(openid)) {
            throw UnauthorizedException("你没有权限进行查看")
        }
        return userService.getUser(id).toViewModel()
    }

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
        userService.deleteUserByOpenid(openid)
    }

    //可能管理员需要删除某个成员
    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
        @CurrentUserOpenId openid: String
    ) {
        if (!userService.checkIsSuperAdmin(openid)) {
            throw UnauthorizedException("你没有权限删除用户")
        }
        userService.deleteUser(id)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginModel: LoginRequestModel
    ): String {
        return userService.login(loginModel.jsCode)
    }
}
