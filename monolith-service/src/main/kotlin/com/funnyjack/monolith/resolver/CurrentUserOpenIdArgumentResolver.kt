package com.funnyjack.monolith.resolver

import com.funnyjack.monolith.annotation.CurrentUserOpenId
import com.funnyjack.monolith.util.JwtUtil
import com.hiczp.spring.error.UnauthorizedException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserOpenIdArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUserOpenId::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): String? {
        val request = webRequest.nativeRequest as jakarta.servlet.http.HttpServletRequest
        val token = request.getHeader("Authorization")?.replace("Bearer ", "")

        // 如果 token 不存在或无效，返回 401 错误
        if (token == null || !JwtUtil.validateToken(token)) {
            throw UnauthorizedException("登录信息过期，请重新登录")
        }

        return JwtUtil.getOpenidFromToken(token)
    }
}
