package com.udemy.course.kafka.twitter.config

import com.typesafe.config.ConfigFactory

object Config {
    val config = ConfigFactory.load()

    val SERVER_PORT: Int = config.getInt("server.port")
    val SERVER_SHUTDOWN_SECS: Long = config.getLong("server.shutdown-timeout")

    val BASE_ROUTE_URL: String = config.getString("route.base-route")

    val TWITTER_STREAM_URL: String = config.getString("url.twitter-stream")
    val TWITTER_RECENT_QUERY: String = config.getString("url.twitter-recent-query")
    val TWITTER_API_KEY: String = config.getString("key.twitter-api")
}