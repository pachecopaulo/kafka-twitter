package com.udemy.course.kafka.twitter.exception

sealed class ErrorExceptions() : Throwable() {
    abstract val errorContent: ErrorContent<Any>?

    data class PropertyValidationException(
        val invalidProperty: String,
        val invalidValue: String? = null,
        override val message: String? = "InvalidProperty $invalidProperty",
        override val cause: Throwable? = null,
        override val errorContent: ErrorContent<Any>? = null
    ) : ErrorExceptions()
}

abstract class ErrorContent<T>(
    open val errorCode: Long,
    open val details: T
)