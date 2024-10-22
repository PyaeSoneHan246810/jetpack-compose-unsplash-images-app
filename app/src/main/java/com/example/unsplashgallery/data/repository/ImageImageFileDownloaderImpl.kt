package com.example.unsplashgallery.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.unsplashgallery.domain.repository.ImageFileDownloader
import java.io.File

class ImageImageFileDownloaderImpl(
    context: Context
): ImageFileDownloader {
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override fun downloadImageFile(url: String, fileName: String?) {
        try {
            val title = fileName ?: "New Image"
            val request = DownloadManager
                .Request(url.toUri())
                .setMimeType("image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(title)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "${File.separator}$title.jpg"
                )
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}