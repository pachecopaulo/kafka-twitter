package com.udemy.course.kafka.twitter.configuration

import com.udemy.course.kafka.twitter.api.TwitterAPIHandler
import com.udemy.course.kafka.twitter.api.TwitterAPIRoutes
import com.udemy.course.kafka.twitter.configuration.Config.APIConfig.apiBaseRoute
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

fun allBeans() = beans {
    beans(this)
    filterBeans(this)
    routeBeans(this)
}

private fun filterBeans(ctx: BeanDefinitionDsl) = with(ctx) {
    bean { CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() } }
}

private fun beans(ctx: BeanDefinitionDsl) = with(ctx) {
    bean<TwitterAPIRoutes>()
    bean { TwitterAPIHandler() }
}

private fun routeBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(
            router {
                apiBaseRoute.value.nest {
                    ref<TwitterAPIRoutes>().route(this)
                }
            }, HandlerStrategies.withDefaults()
        )
    }
}