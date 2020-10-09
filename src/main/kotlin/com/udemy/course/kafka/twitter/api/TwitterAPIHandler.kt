package com.udemy.course.kafka.twitter.api

import com.udemy.course.kafka.twitter.configuration.Config.AuthorizationConfig.twitterAPIKey
import com.udemy.course.kafka.twitter.configuration.Config.EndpointConfig.twitterRecentSearchEndpoint
import com.udemy.course.kafka.twitter.configuration.Config.EndpointConfig.twitterStreamEndpoint
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull
import org.springframework.web.reactive.function.server.sse
import java.lang.IllegalArgumentException

class TwitterAPIHandler(private val webClient: WebClient) {
    suspend fun consumeStreamFromTwitter(req: ServerRequest): ServerResponse {
        val apiCall = webClient.get()
            .uri(twitterStreamEndpoint.value)
            .header("Authorization", "Bearer ${twitterAPIKey.value}")
            .accept(APPLICATION_OCTET_STREAM)
            .retrieve()
            .bodyToFlow<ByteArray>()

        return ServerResponse.ok().sse().bodyAndAwait(apiCall)
    }

    suspend fun findRecentTweets(request: ServerRequest): ServerResponse {
        val querySearch = request
            .queryParamOrNull("query")
            ?: throw IllegalArgumentException("Invalid argument for query")

        require(querySearch.length <= MAX_TWITTER_CHAR_SIZE)  {
            "Rule for matching Tweets must have up to $MAX_TWITTER_CHAR_SIZE characters"
        }

        val apiCall = webClient.get()
            .uri(twitterRecentSearchEndpoint.value + querySearch)
            .header("Authorization", "Bearer ${twitterAPIKey.value}")
            .accept(APPLICATION_JSON)
            .retrieve()
            .bodyToFlow<ByteArray>()

        return ServerResponse.ok().bodyAndAwait(apiCall)
    }

    companion object {
        private const val MAX_TWITTER_CHAR_SIZE = 512
    }
}