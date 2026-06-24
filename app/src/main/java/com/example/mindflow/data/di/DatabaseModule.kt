package com.example.mindflow.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE ideas RENAME COLUMN category TO categories"
            )
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindflow_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
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
