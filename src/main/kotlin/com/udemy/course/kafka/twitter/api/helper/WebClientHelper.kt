package com.udemy.course.kafka.twitter.api.helper

import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


private val logger = KotlinLogging.logger {}

fun WebClient.defaultGet(uri: String): Mono<ClientResponse> = this.get()
    .uri(uri)
    .accept(MediaType.APPLICATION_JSON)
    .exchange()
    .doOnEach { logger.info("Response received from call to $uri: $it") }
    .doOnError { logger.error("Error occured while making call to $uri.", it) }