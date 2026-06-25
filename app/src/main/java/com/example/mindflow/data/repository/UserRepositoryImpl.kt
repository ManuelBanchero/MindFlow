package com.example.mindflow.data.repository

import android.database.sqlite.SQLiteException
import retrofit2.HttpException
import com.example.mindflow.data.local.room.dao.UserDAO
import com.example.mindflow.data.local.preferences.SessionPreferencesDataSource
import com.example.mindflow.data.mapper.toDomain
import com.example.mindflow.data.mapper.toEntity
import com.example.mindflow.data.remote.datasource.UserRemoteDataSource
import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.RegisterRequest
import com.example.mindflow.data.remote.dto.UserDTO
import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.model.error.AuthError
import com.example.mindflow.domain.model.param.RegistrationForm
import com.example.mindflow.domain.model.param.LoginForm
import com.example.mindflow.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDAO,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val sessionPreferencesDataSource: SessionPreferencesDataSource
): UserRepository {
    override suspend fun createUser(registrationForm: RegistrationForm): Result<Unit> {
        return try {
            val registrationRequest = RegisterRequest(
                firstName = registrationForm.firstName,
                lastName = registrationForm.lastName,
                mail = registrationForm.mail,
                password = registrationForm.password
            )

            // First create remote user
            val user: UserDTO = userRemoteDataSource.register(registrationRequest)
            // Save user locally
            userDao.insertUser(user.toEntity())
            sessionPreferencesDataSource.setSessionActive(true)

            Result.success(Unit)
        } catch (e: HttpException) {
            val customException = when (e.code()) {
                409 -> AuthError.EmailAlreadyExistsException
                else -> AuthError.UnknownAuthException
            }
            Result.failure(customException)
        } catch (e: IOException) {
          Result.failure(AuthError.NetworkTimeoutException)
        } catch (e: SQLiteException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun validateCredentials(loginForm: LoginForm): Result<Unit> {
        return try {
            val loginRequest = LoginRequest(
                mail = loginForm.mail,
                password = loginForm.password
            )

            // Validate user credentials and get user data
            val user: UserDTO = userRemoteDataSource.logIn(loginRequest)

            // Save user locally
            userDao.insertUser(user.toEntity())
            sessionPreferencesDataSource.setSessionActive(true)

            Result.success(Unit)
        } catch (e: HttpException) {
            val customException = when (e.code()) {
                401 -> AuthError.InvalidCredentialsException
                else -> AuthError.UnknownAuthException
            }
            Result.failure(customException)
        } catch (e: IOException) {
            Result.failure(AuthError.NetworkTimeoutException)
        } catch (e: SQLiteException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getActiveSession(): User? {
        val activeUser = userDao.getActiveUser()
        return activeUser?.toDomain()
    }

    override suspend fun isSessionActive(): Boolean {
        return sessionPreferencesDataSource.isSessionActive()
    }

    override suspend fun logOut(): Result<Unit> {
        return try {
            userDao.deleteUser()
            sessionPreferencesDataSource.setSessionActive(false)
            Result.success(Unit)
        } catch (e: SQLiteException) {
            runCatching { sessionPreferencesDataSource.setSessionActive(false) }
            Result.failure(e)
        } catch (e: Exception) {
            runCatching { sessionPreferencesDataSource.setSessionActive(false) }
            Result.failure(e)
        }
    }

    override suspend fun subscribeToPlan(userId: Int): Result<User> {
        return try {
            // Subscribe user to plan online
            userRemoteDataSource.subscribeToPlan(userId)
            // Set user has subscribed to plan locally
            userDao.updateSubscriptionStatus(userId, isSubscribed = true)
            val user = userDao.getActiveUser() ?: return Result.failure(Exception("Ocurrió un error al tratar de obtener el usuario"))

            Result.success(user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
