package com.example.unsplashgallery.domain.repository

interface ImageDownloadManager {
    fun downloadImageFile(url: String, fileName: String?)
}