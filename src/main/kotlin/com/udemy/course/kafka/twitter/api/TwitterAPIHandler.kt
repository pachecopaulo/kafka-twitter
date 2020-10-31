package com.udemy.course.kafka.twitter.api

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.fx
import arrow.fx.reactor.fix
import arrow.fx.reactor.k
import com.udemy.course.kafka.twitter.api.helper.defaultGet
import com.udemy.course.kafka.twitter.api.helper.okJson
import com.udemy.course.kafka.twitter.api.helper.okJsonList
import com.udemy.course.kafka.twitter.api.helper.queryParamAsString
import com.udemy.course.kafka.twitter.configuration.Config.AuthorizationConfig.twitterAPIKey
import com.udemy.course.kafka.twitter.configuration.Config.EndpointConfig.twitterRecentSearchEndpoint
import mu.KotlinLogging
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.extra.retry.retryRandomBackoff
import java.time.Duration

class TwitterAPIHandler {

    private val webClient = WebClient.builder()
        .baseUrl(twitterRecentSearchEndpoint.value)
        .defaultHeader("Authorization", "Bearer ${twitterAPIKey.value}")
        .exchangeStrategies(ExchangeStrategies.withDefaults())
        .build()

    fun findTweetsByKeyWord(request: ServerRequest) : Mono<ServerResponse> = okJson {
       MonoK.fx {
            val keySearch = queryParamAsString(request, "query")
            val res = webClient
                .defaultGet("recent?query=$keySearch")
                .flatMap { it.bodyToMono(String::class.java) }
                .retryRandomBackoff(RETRY_TIMES, Duration.ofMillis(RETRY_MIN_DELAY), Duration.ofMillis(RETRY_MAX_DELAY)) { Unit }
                .k()
           res
        }
    }

    companion object {
        private const val RETRY_TIMES = 3L
        private const val RETRY_MIN_DELAY = 1000L
        private const val RETRY_MAX_DELAY = 2000L
    }
}