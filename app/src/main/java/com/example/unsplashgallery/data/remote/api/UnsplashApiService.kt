package com.example.unsplashgallery.data.remote.api

import com.example.unsplashgallery.BuildConfig
import com.example.unsplashgallery.data.remote.dto.UnsplashImageDto
import com.example.unsplashgallery.data.remote.dto.UnsplashImagesSearchResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApiService {
    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getEditorialFeedImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<UnsplashImageDto>

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos/{id}")
    suspend fun getSingleImage(
        @Path("id") imageId: String
    ): UnsplashImageDto

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun getSearchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UnsplashImagesSearchResult
}