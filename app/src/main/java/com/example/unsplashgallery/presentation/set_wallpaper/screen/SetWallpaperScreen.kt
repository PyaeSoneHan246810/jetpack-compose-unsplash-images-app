package com.example.unsplashgallery.presentation.set_wallpaper.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.unsplashgallery.R
import com.example.unsplashgallery.presentation.common.components.AnimatedLoadingIndicator
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetWallpaperScreen(
    modifier: Modifier = Modifier,
    imageUrl: String,
    isLoading: Boolean,
    snackBarEventFlow: Flow<SnackBarEvent>,
    onConfirmClick: (imageUrl: String) -> Unit
) {
    //error snack bar state
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = true) {
        snackBarEventFlow.collect { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = {
                    Snackbar(
                        modifier = Modifier
                            .padding(bottom = 32.dp),
                        snackbarData = it,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(12.dp),
                    )
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(
                        model = imageUrl
                    ),
                    contentDescription = stringResource(R.string.wallpaper_image),
                    contentScale = ContentScale.Crop
                )
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = 12.dp
                        )
                        .navigationBarsPadding(),
                    visible = !isLoading,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it / 2 }
                    ) + fadeOut()
                ) {
                    IconButton(
                        onClick = {
                            onConfirmClick(imageUrl)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.confirm)
                        )
                    }
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.Center),
                    visible = isLoading,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedLoadingIndicator()
                    }
                }
            }
        }
    }
}