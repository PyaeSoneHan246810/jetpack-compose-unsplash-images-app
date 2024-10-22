package com.example.unsplashgallery.domain.repository

interface Downloader {
    fun downloadFile(url: String, fileName: String?)
}