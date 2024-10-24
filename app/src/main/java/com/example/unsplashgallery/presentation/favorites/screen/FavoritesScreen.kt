package com.example.unsplashgallery.presentation.favorites.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.presentation.common.components.EmptyContent
import com.example.unsplashgallery.presentation.common.components.MainTopAppBar
import com.example.unsplashgallery.presentation.common.components.PreviewImageCard
import com.example.unsplashgallery.presentation.common.components.UnsplashImageCardGrids
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import com.example.unsplashgallery.presentation.common.utils.rememberWindowInsetsControllerCompat
import com.example.unsplashgallery.presentation.common.utils.toggleSystemBarsVisibility
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    unsplashImages: LazyPagingItems<UnsplashImage>?,
    favoriteImagesIds: List<String>,
    snackBarEventFlow: Flow<SnackBarEvent>,
    onImageCardClick: (imageId: String) -> Unit,
    onToggleFavoriteStatus: (image: UnsplashImage) -> Unit,
    onSearchIconButtonClick: () -> Unit,
    onArrowBackIconButtonClick: () -> Unit,
) {
    //system bar visibility
    val windowInsetsControllerCompact = rememberWindowInsetsControllerCompat()
    LaunchedEffect(key1 = Unit) {
        windowInsetsControllerCompact.toggleSystemBarsVisibility(
            visible = true
        )
    }
    //preview image state
    var isPreviewImageCardVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var previewImage by remember {
        mutableStateOf<UnsplashImage?>(null)
    }
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                MainTopAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    title = stringResource(R.string.favorite_images),
                    navigationIconVisible = true,
                    onSearchIconButtonClick = onSearchIconButtonClick,
                    onArrowBackIconButtonClick = onArrowBackIconButtonClick
                )
            },
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
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    )
            ) {
                unsplashImages?.let {
                    if (it.itemCount == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyContent(
                                imageVector = Icons.Rounded.Image,
                                message = stringResource(R.string.empty_favorite_message)
                            )
                        }
                    } else {
                        UnsplashImageCardGrids(
                            bottomContentPadding = paddingValues.calculateBottomPadding(),
                            unsplashImages = it,
                            favoriteImagesIds = favoriteImagesIds,
                            onImageCardClick = { imageId ->
                                if (!isPreviewImageCardVisible) onImageCardClick(imageId)
                            },
                            onToggleFavoriteStatus = onToggleFavoriteStatus,
                            onImageCardDragStart = { image ->
                                isPreviewImageCardVisible = true
                                previewImage = image
                            },
                            onImageCardDragEnd = {
                                isPreviewImageCardVisible = false
                                previewImage = null
                            },
                            onImageCardDragCancel = {
                                isPreviewImageCardVisible = false
                                previewImage = null
                            }
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            visible = isPreviewImageCardVisible && previewImage != null
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)
                        )
                )
                PreviewImageCard(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp
                        ),
                    previewImage = previewImage
                )
            }
        }
    }
}