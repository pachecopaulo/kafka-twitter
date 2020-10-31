package com.udemy.course.kafka.twitter.api

import org.springframework.web.reactive.function.server.RouterFunctionDsl

data class TwitterAPIRoutes(private val handler: TwitterAPIHandler) {

    fun route(router: RouterFunctionDsl) = with(router) {
        GET("/recent", handler::findTweetsByKeyWord)
    }
}