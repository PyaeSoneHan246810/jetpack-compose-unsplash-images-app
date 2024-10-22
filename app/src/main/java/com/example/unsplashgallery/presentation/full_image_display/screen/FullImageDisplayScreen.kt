package com.example.unsplashgallery.presentation.full_image_display.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.presentation.common.components.AnimatedLoadingIndicator
import com.example.unsplashgallery.presentation.common.components.ImageDownloadOption
import com.example.unsplashgallery.presentation.common.components.ImageDownloadOptionsModalBottomSheet
import com.example.unsplashgallery.presentation.full_image_display.component.FullImageDisplayTopAppBar
import com.example.unsplashgallery.utils.SnackBarEvent
import com.example.unsplashgallery.utils.rememberWindowInsetsControllerCompat
import com.example.unsplashgallery.utils.toggleSystemBarsVisibility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.max

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullImageDisplayScreen(
    modifier: Modifier = Modifier,
    unsplashImage: UnsplashImage?,
    snackBarEventFlow: Flow<SnackBarEvent>,
    onNavigateUp: () -> Unit,
    onPhotographerInfoClick: (profileLink: String) -> Unit,
    onDownloadOptionClick: (url: String, title: String?) -> Unit,
    onSetWallpaperButtonClick: (url: String) -> Unit
) {
    //context
    val context = LocalContext.current
    //coroutine scope
    val scope = rememberCoroutineScope()
    //visibility states
    var isTopAppBarVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isSetWallpaperButtonVisible by rememberSaveable {
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
    val modalBottomSheetState = rememberModalBottomSheetState()
    var isModalBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    //image states
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
    //downloading toast message
    val downloadingToastMessage = stringResource(R.string.downloading)
    //error snack bar state
    val snackBarHostState = SnackbarHostState()
    LaunchedEffect(key1 = true) {
        snackBarEventFlow.collect { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }
    //back handler
    BackHandler {
        if (isModalBottomSheetVisible) {
            scope.launch {
                modalBottomSheetState.hide()
            }.invokeOnCompletion {
                isModalBottomSheetVisible = false
            }
        } else {
            onNavigateUp()
        }
    }
    //screen content
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
                    .fillMaxSize()
                    .clickable {
                        isTopAppBarVisible = !isTopAppBarVisible
                        isSetWallpaperButtonVisible = !isSetWallpaperButtonVisible
                        isSystemBarsVisible = !isSystemBarsVisible
                        windowInsetsControllerCompact.toggleSystemBarsVisibility(
                            visible = isSystemBarsVisible
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                //top app bar
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
                        onArrowBackIconButtonClick = onNavigateUp,
                        onDownloadIconButtonClick = {
                            scope.launch {
                                modalBottomSheetState.show()
                            }.invokeOnCompletion {
                                isModalBottomSheetVisible = true
                            }
                        },
                        onPhotographerInfoClick = onPhotographerInfoClick
                    )
                }
                //image loading indicator
                if (isLoadingImage) {
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
                                    isSetWallpaperButtonVisible = !isSetWallpaperButtonVisible
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
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    visible = isSetWallpaperButtonVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it / 2 }
                    ) + fadeOut()
                ) {
                    Button(
                        modifier = Modifier
                            .padding(
                                bottom = 12.dp
                            )
                            .navigationBarsPadding(),
                        onClick = {
                            unsplashImage?.let { image ->
                                onSetWallpaperButtonClick(image.imageUrlRaw)
                            }
                        }
                    ) {
                        Text(
                            text = "Set Wallpaper"
                        )
                    }
                }
                //modal bottom sheet
                if (isModalBottomSheetVisible) {
                    ImageDownloadOptionsModalBottomSheet(
                        sheetState = modalBottomSheetState,
                        onDismissRequest = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }.invokeOnCompletion {
                                isModalBottomSheetVisible = false
                            }
                        },
                        onDownloadOptionClick = { option ->
                            scope.launch {
                                modalBottomSheetState.hide()
                            }.invokeOnCompletion {
                                isModalBottomSheetVisible = false
                            }
                            unsplashImage?.let { image ->
                                val url = when(option) {
                                    ImageDownloadOption.SMALL_SIZE -> {
                                        image.imageUrlSmall
                                    }
                                    ImageDownloadOption.MEDIUM_SIZE -> {
                                        image.imageUrlRegular
                                    }
                                    ImageDownloadOption.ORIGINAL_SIZE -> {
                                        image.imageUrlRaw
                                    }
                                }
                                onDownloadOptionClick(url, image.description?.take(20))
                                Toast.makeText(context, downloadingToastMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}