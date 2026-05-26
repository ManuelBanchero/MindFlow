package com.example.mindflow.data.repository

import android.database.sqlite.SQLiteException
import retrofit2.HttpException
import com.example.mindflow.data.local.entity.UserEntity
import com.example.mindflow.data.local.room.dao.UserDAO
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

class UserRepositoryImp(
    private val userDao: UserDAO,
    private val userRemoteDataSource: UserRemoteDataSource
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

            // Create user local entity
            val userEntity = UserEntity(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                mail = user.mail,
                isSubscribed = user.isSubscribed
            )
            // Save user locally
            userDao.insertUser(userEntity)

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

    override suspend fun logOut(): Result<Unit> {
        return try {
            userDao.deleteUser()
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun subscribeToPlan(userId: Int): Result<Unit> {
        return try {
            // Subscribe user to plan online
            userRemoteDataSource.subscribeToPlan(userId)
            // Set user has subscribed to plan locally
            userDao.updateSubscriptionStatus(userId, isSubscribed = true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}