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

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT first_name FROM users WHERE id = :userId")
    suspend fun getUserFirstName(userId: Int): String?

    @Query("SELECT is_subscribed FROM users WHERE id = :userId")
    fun getUserIsSubscribed(userId: Int): Flow<Boolean>
}