package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.param.LoginForm
import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository
import com.example.mindflow.domain.validator.AuthValidator
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(form: LoginForm): Result<Unit> {
        if (form.mail.isBlank() || form.password.isBlank())
            return Result.failure(Exception("Ambos campos deben completarse"))
        if (!AuthValidator.isEmailValid(form.mail))
            return Result.failure(Exception("Mail inválido"))
        if (!AuthValidator.isPasswordValid(form.password))
            return Result.failure(Exception("La contraseña debe contener al menos 8 carácteres"))

        val loginResult = userRepository.validateCredentials(form)
        if (loginResult.isFailure) {
            return loginResult
        }

        val activeUser = userRepository.getActiveSession()
            ?: return Result.failure(Exception("No pudimos recuperar la sesión activa"))

        val syncResult = ideaRepository.syncUserIdeas(activeUser.id)
        if (syncResult.isFailure) {
            ideaRepository.deleteAllLocalIdeas()
            userRepository.logOut()
            return syncResult
        }

        return loginResult
    }
}
