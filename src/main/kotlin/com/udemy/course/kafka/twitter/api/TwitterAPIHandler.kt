package com.udemy.course.kafka.twitter.api

import com.udemy.course.kafka.twitter.config.Config.TWITTER_API_KEY
import com.udemy.course.kafka.twitter.config.Config.TWITTER_RECENT_QUERY
import com.udemy.course.kafka.twitter.config.Config.TWITTER_STREAM_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.attributeOrNull
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull
import org.springframework.web.reactive.function.server.sse
import java.lang.IllegalArgumentException
import java.net.URLDecoder

class TwitterAPIHandler(private val webClient: WebClient) {

    suspend fun consumeStreamFromTwitter(req: ServerRequest): ServerResponse {
        val apiCall = webClient.get()
            .uri(TWITTER_STREAM_URL)
            .header("Authorization", "Bearer $TWITTER_API_KEY")
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
            .uri(TWITTER_RECENT_QUERY + querySearch)
            .header("Authorization", "Bearer $TWITTER_API_KEY")
            .accept(APPLICATION_JSON)
            .retrieve()
            .bodyToFlow<ByteArray>()

        return ServerResponse.ok().bodyAndAwait(apiCall)
    }

    companion object : KLogging() {
        private const val MAX_TWITTER_CHAR_SIZE = 512
    }
}