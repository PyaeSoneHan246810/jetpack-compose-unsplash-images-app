package com.example.unsplashgallery.domain.repository

interface ImageFileDownloader {
    fun downloadImageFile(url: String, fileName: String?)
}