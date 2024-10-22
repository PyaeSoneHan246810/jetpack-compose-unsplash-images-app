package com.example.unsplashgallery.di

import android.content.Context
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.data.repository.ImageDownloader
import com.example.unsplashgallery.data.repository.ImageRepositoryImpl
import com.example.unsplashgallery.domain.repository.Downloader
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApiService {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val contentType = "application/json".toMediaType()
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UnsplashApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        unsplashApiService: UnsplashApiService
    ): ImageRepository {
        return ImageRepositoryImpl(
            unsplashApiService = unsplashApiService
        )
    }

    @Provides
    @Singleton
    fun provideDownloader(
        @ApplicationContext context: Context
    ): Downloader {
        return ImageDownloader(
            context = context
        )
    }
}