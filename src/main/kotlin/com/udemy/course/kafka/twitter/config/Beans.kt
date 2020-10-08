package com.udemy.course.kafka.twitter.config

import com.udemy.course.kafka.twitter.api.TwitterAPIHandler
import com.udemy.course.kafka.twitter.api.TwitterAPIRoute
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

fun allBeans() = beans {
    beans(this)
    filterBeans(this)
    routeBeans(this)
}

private fun filterBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean { CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() } }
}

private fun beans(ctx: BeanDefinitionDsl) = with (ctx) {
    val webClient = WebClient.builder().build()
    bean<TwitterAPIRoute>()
    bean {
        TwitterAPIHandler(webClient)
    }
}

private fun routeBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(
            router {
                Config.BASE_ROUTE_URL.and(accept(MediaType.APPLICATION_JSON)).nest {
                    add(ref<TwitterAPIRoute>().routes)
                }
            }, HandlerStrategies.withDefaults()
        )
    }
}