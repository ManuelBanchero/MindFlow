package com.example.mindflow.data.di

import com.example.mindflow.data.local.file.FileManager
import com.example.mindflow.data.local.file.impl.InternalFileManager
import com.example.mindflow.data.local.hardware.AudioDataSource
import com.example.mindflow.data.local.hardware.SpeechToTextDataSource
import com.example.mindflow.data.local.hardware.impl.AndroidAudioDataSource
import com.example.mindflow.data.local.hardware.impl.MockSpeechToTextDataSource
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.datasource.UserRemoteDataSource
import com.example.mindflow.data.remote.datasource.impl.MockIdeaProcessorDataSourceImpl
import com.example.mindflow.data.remote.datasource.impl.MockIdeaRemoteDataSource
import com.example.mindflow.data.remote.datasource.impl.MockUserRemoteDataSource
import com.example.mindflow.data.remote.datasource.impl.RetrofitIdeaProcessorDataSourceImpl
import com.example.mindflow.data.remote.datasource.impl.RetrofitIdeaRemoteDataSourceImpl
import com.example.mindflow.data.repository.IdeaRepositoryImpl
import com.example.mindflow.data.repository.UserRepositoryImpl
import com.example.mindflow.data.service.AudioRecorderImpl
import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository
import com.example.mindflow.domain.service.AudioRecorder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindIdeaRepository(
        ideaRepositoryImpl: IdeaRepositoryImpl
    ): IdeaRepository

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        mockUserRemoteDataSource: MockUserRemoteDataSource
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindIdeaRemoteDataSource(
        //mockIdeaRemoteDataSource: MockIdeaRemoteDataSource
        retrofitIdeaRemoteDataSource: RetrofitIdeaRemoteDataSourceImpl
    ): IdeaRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindIdeaProcessorDataSource(
        //mockIdeaProcessorDataSource: MockIdeaProcessorDataSourceImpl
        retrofitIdeaProcessorDataSource: RetrofitIdeaProcessorDataSourceImpl
    ): IdeaProcessorDataSource

    @Binds
    @Singleton
    abstract fun bindSpeechToTextDataSource(
        mockSpeechToTextDataSource: MockSpeechToTextDataSource
    ): SpeechToTextDataSource

    @Binds
    @Singleton
    abstract fun bindFileManager(
        internalFileManager: InternalFileManager
    ): FileManager

    @Binds
    @Singleton
    abstract fun bindAudioDataSource(
        androidAudioDataSource: AndroidAudioDataSource
    ): AudioDataSource

    @Binds
    @Singleton
    abstract fun bindAudioRecorder(
        audioRecorderImpl: AudioRecorderImpl
    ): AudioRecorder
}