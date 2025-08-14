package com.funnyjack.monolith.config

import com.funnyjack.monolith.resolver.CurrentUserOpenIdArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val currentUserOpenIdArgumentResolver: CurrentUserOpenIdArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentUserOpenIdArgumentResolver)
    }
}
