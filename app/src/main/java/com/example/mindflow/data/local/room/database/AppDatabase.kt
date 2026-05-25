package com.example.mindflow.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.local.entity.UserEntity
import com.example.mindflow.data.local.room.DAO.IdeaDAO
import com.example.mindflow.data.local.room.DAO.UserDAO

@Database(entities = [
    IdeaEntity::class,
    QuestionEntity::class,
    UserEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ideaDao(): IdeaDAO

    abstract fun userDao(): UserDAO
}