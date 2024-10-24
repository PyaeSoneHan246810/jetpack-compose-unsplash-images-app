package com.example.unsplashgallery.di

import android.content.Context
import androidx.room.Room
import com.example.unsplashgallery.data.local.database.AppDatabase
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.data.repository.ImageDownloadManagerImpl
import com.example.unsplashgallery.data.repository.ImageRepositoryImpl
import com.example.unsplashgallery.data.repository.ImageWallpaperManagerImpl
import com.example.unsplashgallery.data.repository.NetworkConnectivityManagerImpl
import com.example.unsplashgallery.domain.repository.ImageDownloadManager
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.domain.repository.ImageWallpaperManager
import com.example.unsplashgallery.domain.repository.NetworkConnectivityManager
import com.example.unsplashgallery.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApiService(
        okHttpClient: OkHttpClient
    ): UnsplashApiService {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val contentType = "application/json".toMediaType()
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UnsplashApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = Constants.DATABASE
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        unsplashApiService: UnsplashApiService,
        appDatabase: AppDatabase,
    ): ImageRepository {
        return ImageRepositoryImpl(
            unsplashApiService = unsplashApiService,
            appDatabase = appDatabase,
        )
    }

    @Provides
    @Singleton
    fun provideImageDownloadManager(
        @ApplicationContext context: Context
    ): ImageDownloadManager {
        return ImageDownloadManagerImpl(
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideImageWallpaperManager(
        @ApplicationContext context: Context
    ): ImageWallpaperManager {
        return ImageWallpaperManagerImpl(
            context = context
        )
    }

    @Provides
    @Singleton
    fun providesNetworkConnectivityManager(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): NetworkConnectivityManager {
        return NetworkConnectivityManagerImpl(
            context = context,
            scope = scope,
        )
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}