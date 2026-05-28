package com.example.mindflow.data.di

import android.content.Context
import androidx.room.Room
import com.example.mindflow.data.local.room.dao.IdeaDAO
import com.example.mindflow.data.local.room.dao.UserDAO
import com.example.mindflow.data.local.room.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindflow_db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDAO {
        return database.userDao()
    }

    @Provides
    fun provideIdeaDao(database: AppDatabase): IdeaDAO {
        return database.ideaDao()
    }
}
