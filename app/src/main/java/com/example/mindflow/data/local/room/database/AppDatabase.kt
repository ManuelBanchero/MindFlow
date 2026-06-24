package com.example.mindflow.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.local.entity.UserEntity
import com.example.mindflow.data.local.room.dao.IdeaDAO
import com.example.mindflow.data.local.room.dao.UserDAO

@Database(entities = [
    IdeaEntity::class,
    QuestionEntity::class,
    UserEntity::class],
    version = 2,
    exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ideaDao(): IdeaDAO
    abstract fun userDao(): UserDAO
}
