package com.udemy.course.kafka.twitter.api.helper

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.k
import arrow.fx.reactor.value
import com.udemy.course.kafka.twitter.exception.ErrorExceptions
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun queryParamAsString(request: ServerRequest, param: String): MonoK<String> = Mono
    .just(request.queryParam(param).get())
    .onErrorMap { t ->
        ErrorExceptions.PropertyValidationException(
            invalidProperty = param,
            cause = t,
            message = "Query Parameter {$param} can't be converted to a string")
    }.k()

inline fun <reified T> okJson(p: () -> MonoK<T>) = ServerResponse
    .ok().contentType(APPLICATION_JSON)
    .body(BodyInserters.fromPublisher(p().value(), T::class.java))

inline fun <reified T> okJsonList(p: () -> MonoK<List<T>>): Mono<ServerResponse> {
    val pType = object : ParameterizedTypeReference<List<T>>() {}
    return ServerResponse.ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(p().value(), pType))
}

