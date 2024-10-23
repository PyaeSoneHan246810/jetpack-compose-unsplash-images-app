package com.example.unsplashgallery.presentation.common.utils

import androidx.compose.material3.SnackbarDuration

data class SnackBarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
