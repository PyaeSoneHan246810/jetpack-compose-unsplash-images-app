package com.example.unsplashgallery.di

import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApiService {
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UnsplashApiService::class.java)
    }
}