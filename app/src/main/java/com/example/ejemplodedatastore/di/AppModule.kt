package com.example.ejemplodedatastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.example.ejemplodedatastore.AppStatsProto
import com.example.ejemplodedatastore.data.AppStatsRepository
import com.example.ejemplodedatastore.data.AppStatsRepositoryImpl
import com.example.ejemplodedatastore.data.AppStatsSerializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppStatsRepository(
        appStatsRepositoryImpl: AppStatsRepositoryImpl
    ): AppStatsRepository

    companion object {
        @Provides
        @Singleton
        fun provideProtoDataStore(
            @ApplicationContext appContext: Context
        ): DataStore<AppStatsProto> {
            return DataStoreFactory.create(
                serializer = AppStatsSerializer,
                produceFile = { appContext.dataStoreFile("app_stats.pb") },
                corruptionHandler = ReplaceFileCorruptionHandler {
                    AppStatsProto.getDefaultInstance() // Valor seguro de recuperación
                }
            )
        }
    }
}