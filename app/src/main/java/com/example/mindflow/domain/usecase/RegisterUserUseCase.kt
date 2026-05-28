package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.param.RegistrationForm
import com.example.mindflow.domain.repository.UserRepository
import com.example.mindflow.domain.validator.AuthValidator
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(form: RegistrationForm): Result<Unit> {
        if (
            form.firstName.isBlank() ||
            form.lastName.isBlank() ||
            form.mail.isBlank() ||
            form.password.isBlank()
        ) {
            return Result.failure(Exception("Todos los campos deben completarse"))
        }
        if (!AuthValidator.isEmailValid(form.mail))
            return Result.failure(Exception("Mail inválido"))
        if (!AuthValidator.isNameValid(form.firstName))
            return Result.failure(Exception("Nombre inválido"))
        if (!AuthValidator.isNameValid(form.lastName))
            return Result.failure(Exception("Apellido inválido"))

        return userRepository.createUser(form)
    }
}