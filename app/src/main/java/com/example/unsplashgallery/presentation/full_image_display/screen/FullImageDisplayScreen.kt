package com.example.unsplashgallery.presentation.full_image_display.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.presentation.common.components.AnimatedLoadingIndicator
import com.example.unsplashgallery.presentation.full_image_display.component.FullImageDisplayTopAppBar
import com.example.unsplashgallery.utils.rememberWindowInsetsControllerCompat
import com.example.unsplashgallery.utils.toggleSystemBarsVisibility
import kotlinx.coroutines.launch
import kotlin.math.max

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullImageDisplayScreen(
    modifier: Modifier = Modifier,
    unsplashImage: UnsplashImage?,
    onArrowBackIconButtonClick: () -> Unit,
    onDownloadIconButtonClick: () -> Unit,
    onPhotographerInfoClick: (profileLink: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isTopAppBarVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isSystemBarsVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val windowInsetsControllerCompact = rememberWindowInsetsControllerCompat()
    LaunchedEffect(key1 = Unit) {
        windowInsetsControllerCompact.toggleSystemBarsVisibility(
            visible = isSystemBarsVisible
        )
    }
    var isLoadingImage by rememberSaveable {
        mutableStateOf(true)
    }
    var isErrorImage by rememberSaveable {
        mutableStateOf(false)
    }
    val asyncImagePainter = rememberAsyncImagePainter(
        model = unsplashImage?.imageUrlRaw,
        onState = { state ->
            isLoadingImage = state is AsyncImagePainter.State.Loading
            isErrorImage = state is AsyncImagePainter.State.Error
        },
    )
    var scale by rememberSaveable {
        mutableFloatStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = max(scale * zoomChange, 1f)
        offset = Offset(
            x = offset.x + offsetChange.x,
            y = offset.y + offsetChange.y
        )
    }
    val isImageZoomed by remember {
        derivedStateOf {
            scale != 1f
        }
    }
    val imageInteractionSource = remember {
        MutableInteractionSource()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
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
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    visible = isTopAppBarVisible,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    FullImageDisplayTopAppBar(
                        photographerProfileImageUrl = unsplashImage?.photographerProfileImageUrl,
                        photographerName = unsplashImage?.photographerName,
                        photographerUsername = unsplashImage?.photographerUsername,
                        photographerProfileLink = unsplashImage?.photographerProfileLink,
                        onArrowBackIconButtonClick = onArrowBackIconButtonClick,
                        onDownloadIconButtonClick = onDownloadIconButtonClick,
                        onPhotographerInfoClick = onPhotographerInfoClick
                    )
                }
                if (isLoadingImage) {
                    //image loading indicator
                    AnimatedLoadingIndicator()
                }
                if (isErrorImage) {
                    //image error message
                    Text(
                        text = stringResource(R.string.image_loading_error)
                    )
                } else {
                    //image
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformable(
                                state = transformableState
                            )
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            }
                            .combinedClickable(
                                onDoubleClick = {
                                    if (isImageZoomed) {
                                        scale = 1f
                                        offset = Offset.Zero
                                    } else {
                                        scope.launch {
                                            transformableState.animateZoomBy(
                                                zoomFactor = 3f,
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    isTopAppBarVisible = !isTopAppBarVisible
                                    isSystemBarsVisible = !isSystemBarsVisible
                                    windowInsetsControllerCompact.toggleSystemBarsVisibility(
                                        visible = isSystemBarsVisible
                                    )
                                },
                                indication = null,
                                interactionSource = imageInteractionSource
                            ),
                        painter = asyncImagePainter,
                        contentDescription = unsplashImage?.description,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}