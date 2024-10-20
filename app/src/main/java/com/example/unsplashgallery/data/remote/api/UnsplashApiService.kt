package com.example.unsplashgallery.data.remote.api

import com.example.unsplashgallery.BuildConfig
import com.example.unsplashgallery.data.remote.dto.UnsplashImageDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface UnsplashApiService {
    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getEditorialFeedImages(): List<UnsplashImageDto>
}