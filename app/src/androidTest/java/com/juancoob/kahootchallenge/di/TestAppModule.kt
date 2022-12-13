package com.juancoob.kahootchallenge.di

import android.app.Application
import androidx.room.Room
import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.kahootchallenge.data.database.QuizDatabase
import com.juancoob.kahootchallenge.data.local.DecoratedLocalDataSource
import com.juancoob.kahootchallenge.data.server.DecoratedRemoteDataSource
import com.juancoob.kahootchallenge.di.qualifier.ApiUrl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

// Provides

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModuleProviderForEndPoint::class]
)
object AppModuleTestProviderForEndPoint {
    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "http://localhost:8080"
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModuleProviderForRoom::class]
)
object AppModuleTestProviderForRoom {
    @Provides
    @Singleton
    fun provideRoomDataBase(app: Application): QuizDatabase =
        Room.inMemoryDatabaseBuilder(
            app,
            QuizDatabase::class.java,
        ).build()
}

// Binds

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModuleBinderForRemoteDataSource::class]
)
abstract class AppModuleTestBinderForDecoratingRemoteDataSourceWithIdlingResources {

    @Binds
    abstract fun bindsRemoteDataSourceTest(
        decoratedRemoteDataSource: DecoratedRemoteDataSource
    ): RemoteDataSource
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModuleBinderForLocalDataSource::class]
)
abstract class AppModuleTestBinderForDecoratingLocalDataSourceWithIdlingResources {

    @Binds
    abstract fun bindsLocalDataSourceTest(
        decoratedLocalDataSource: DecoratedLocalDataSource
    ): LocalDataSource
}
