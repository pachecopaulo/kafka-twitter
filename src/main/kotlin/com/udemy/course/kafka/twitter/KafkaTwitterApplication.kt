package com.udemy.course.kafka.twitter

import com.udemy.course.kafka.twitter.config.Config
import com.udemy.course.kafka.twitter.config.allBeans
import mu.KLogging
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer
import java.time.Duration


class KafkaTwitterApplication {

	private val httpHandler: HttpHandler
	private val server: HttpServer

	init {
		val context = GenericApplicationContext().apply {
			allBeans().initialize(this)
			refresh()
		}
		server = HttpServer.create().port(Config.SERVER_PORT)

		// create the http handler, and assign filters
		httpHandler = WebHttpHandlerBuilder
			.applicationContext(context)
			.build()
	}

	fun startAndAwait() {
		server.handle(ReactorHttpHandlerAdapter(httpHandler))
			.bindUntilJavaShutdown(Duration.ofSeconds(Config.SERVER_SHUTDOWN_SECS)) { _ ->
				logger.info { "Server Started" }
			}
	}

	companion object : KLogging()
}

/**
 * Starts the service, and waits until shutdown signal is received.
 */
fun main(args: Array<String>) {
	KafkaTwitterApplication().startAndAwait()
}
