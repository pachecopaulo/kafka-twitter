package com.udemy.course.kafka.twitter.api

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

data class TwitterAPIRoute(private val handler: TwitterAPIHandler) {
    val routes = coRouter {
        accept(MediaType.APPLICATION_OCTET_STREAM).nest {
            GET("/stream", handler::consumeStreamFromTwitter)
            GET("/recent", handler::findRecentTweets)
        }
    }
}