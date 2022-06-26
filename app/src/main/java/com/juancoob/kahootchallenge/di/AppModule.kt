package com.juancoob.kahootchallenge.di

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.kahootchallenge.data.database.LocalDataSourceImpl
import com.juancoob.kahootchallenge.data.database.QuizDao
import com.juancoob.kahootchallenge.data.database.QuizDatabase
import com.juancoob.kahootchallenge.data.server.RemoteDataSourceImpl
import com.juancoob.kahootchallenge.data.server.RemoteService
import com.juancoob.kahootchallenge.di.qualifier.ApiUrl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

// Provides

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForEndPoint {
    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://create.kahoot.it/rest/kahoots/"
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForRetrofit {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRemoteService(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): RemoteService {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForRoom {
    @Provides
    @Singleton
    fun provideRoomDataBase(app: Application): QuizDatabase =
        Room.databaseBuilder(
            app,
            QuizDatabase::class.java,
            "quiz-database"
        ).build()
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForQuizDao {
    @Provides
    @Singleton
    fun provideQuizDao(quizDatabase: QuizDatabase): QuizDao =
        quizDatabase.quizDao()
}

// Binds

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinderForRemoteDataSource {

    @Binds
    abstract fun bindsRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinderForLocalDataSource {

    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}
