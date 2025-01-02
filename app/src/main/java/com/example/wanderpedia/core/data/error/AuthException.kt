package com.example.wanderpedia.core.data.error

sealed class AuthException(message: String? = null, cause: Throwable? = null) :
    Exception(message, cause) {
    class UserNotFoundException(message: String? = null) : AuthException(message)
}