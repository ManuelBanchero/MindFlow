package com.example.mindflow.domain.validator

object AuthValidator {
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    // Regla de Nombre/Apellido: Solo letras (nativas e internacionales), espacios, guiones y apóstrofes.
    // Longitud de 2 a 50 caracteres para evitar textos masivos maliciosos.
    private val NAME_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\s-]{2,50}\$".toRegex()
    private const val MIN_PASSWORD_LENGTH = 6

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && email.matches(EMAIL_REGEX)
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    fun isNameValid(name: String): Boolean {
        val sanitizedName = name.trim()
        return sanitizedName.isNotBlank() && sanitizedName.matches(NAME_REGEX)
    }
}