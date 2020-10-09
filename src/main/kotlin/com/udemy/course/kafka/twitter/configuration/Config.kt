package com.udemy.course.kafka.twitter.configuration

import com.typesafe.config.ConfigFactory

object Config {
    private val config = lazy { ConfigFactory.load() }

    object ServerConfig {
        private val httpConfig = lazy { config.value.getConfig("server") }

        val port = lazy { httpConfig.value.getInt("port") }
        val shutdownTimeoutSeconds = lazy { httpConfig.value.getLong("shutdown-timeout") }
    }

    object EndpointConfig {
        private val httpConfig = lazy { config.value.getConfig("endpoint") }

        val twitterStreamEndpoint = lazy { httpConfig.value.getString("twitter-stream") }
        val twitterRecentSearchEndpoint = lazy { httpConfig.value.getString("twitter-recent-query") }
    }

    object AuthorizationConfig {
        private val httpConfig = lazy { config.value.getConfig("authorization") }

        val twitterAPIKey = lazy { httpConfig.value.getString("twitter-api-key") }
    }

    object APIConfig {
        private val httpConfig = lazy { config.value.getConfig("api") }

        val apiBaseRoute = lazy { httpConfig.value.getString("base-route") }
    }
}