package com.example.mindflow.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mindflow.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun deleteUser()

    @Query("SELECT first_name FROM users WHERE id = :userId")
    suspend fun getUserFirstName(userId: Int): String?

    @Query("SELECT is_subscribed FROM users WHERE id = :userId")
    fun getUserIsSubscribed(userId: Int): Flow<Boolean>

    @Query("UPDATE users SET is_subscribed = :isSubscribed WHERE id = :userId")
    suspend fun updateSubscriptionStatus(userId: Int, isSubscribed: Boolean)

    /*
        If a user has login or register -> saved locally
        If a user has logout -> is deleted from local database
        So -> If there is an user saved locally, means user has an active session
    */
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getActiveUser(): UserEntity?
}