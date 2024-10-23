package com.example.unsplashgallery.presentation.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.presentation.common.components.MainTopAppBar
import com.example.unsplashgallery.presentation.common.components.PreviewImageCard
import com.example.unsplashgallery.presentation.common.components.UnsplashImageCardGrids
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import com.example.unsplashgallery.presentation.common.utils.rememberWindowInsetsControllerCompat
import com.example.unsplashgallery.presentation.common.utils.toggleSystemBarsVisibility
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    unsplashImages: List<UnsplashImage>,
    snackBarEventFlow: Flow<SnackBarEvent>,
    onImageCardClick: (imageId: String) -> Unit,
    onSearchIconButtonClick: () -> Unit,
    onFavoritesFabClick: () -> Unit,
) {
    //system bar visibility
    val windowInsetsControllerCompact = rememberWindowInsetsControllerCompat()
    LaunchedEffect(key1 = true) {
        windowInsetsControllerCompact.toggleSystemBarsVisibility(
            visible = true
        )
    }
    //top app bar scroll behavior
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    //preview image state
    var isPreviewImageCardVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var previewImage by remember {
        mutableStateOf<UnsplashImage?>(null)
    }
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
    //screen content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                MainTopAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    onSearchIconButtonClick = onSearchIconButtonClick,
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(
                            bottom = 20.dp
                        ),
                    onClick = onFavoritesFabClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(R.string.favorites)
                    )
                }
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
                        top = paddingValues.calculateTopPadding(),
                    )
            ) {
                UnsplashImageCardGrids(
                    bottomContentPadding = paddingValues.calculateBottomPadding(),
                    unsplashImages = unsplashImages,
                    onImageCardClick = { imageId ->
                        if (!isPreviewImageCardVisible) onImageCardClick(imageId)
                    },
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