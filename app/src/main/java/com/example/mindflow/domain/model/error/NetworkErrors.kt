package com.example.mindflow.domain.model.error

sealed class AuthError: Exception() {
    object EmailAlreadyExistsException: AuthError()
    object InvalidCredentialsException: AuthError()
    object NetworkTimeoutException: AuthError()
    object UnknownAuthException: AuthError()
}