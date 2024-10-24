package com.example.unsplashgallery.presentation.search.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.presentation.common.components.PreviewImageCard
import com.example.unsplashgallery.presentation.common.components.UnsplashImageCardGrids
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import com.example.unsplashgallery.presentation.common.utils.rememberWindowInsetsControllerCompat
import com.example.unsplashgallery.presentation.common.utils.toggleSystemBarsVisibility
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    unsplashImages: LazyPagingItems<UnsplashImage>?,
    favoriteImagesIds: List<String>,
    snackBarEventFlow: Flow<SnackBarEvent>,
    onSearchQueryValueChanged: (newQuery: String) -> Unit,
    onSearch: () -> Unit,
    onImageCardClick: (imageId: String) -> Unit,
    onToggleFavoriteStatus: (image: UnsplashImage) -> Unit,
    onFavoritesFabClick: () -> Unit,
    onNavigateUp: () -> Unit
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
    //focus requester for text field
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = true) {
        delay(500)
        focusRequester.requestFocus()
    }
    //suggestions state
    var isSuggestionsVisible by rememberSaveable {
        mutableStateOf(false)
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
                .align(Alignment.TopCenter),
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateUp
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(4.dp)
                        )
                        TextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isSuggestionsVisible = focusState.isFocused
                                },
                            value = searchQuery,
                            onValueChange = onSearchQueryValueChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_images)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.search)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (searchQuery.isNotEmpty()) {
                                            onSearchQueryValueChanged("")
                                        } else {
                                            onNavigateUp()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = stringResource(R.string.clear)
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(100.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search,
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    onSearch()
                                }
                            )
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )
                    AnimatedVisibility(
                        visible = isSuggestionsVisible,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                end = 12.dp,
                                bottom = 12.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = searchSuggestions
                            ) { searchSuggestion ->
                                val searchSuggestionStr = stringResource(searchSuggestion)
                                SuggestionChip(
                                    onClick = {
                                        onSearchQueryValueChanged(searchSuggestionStr)
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        onSearch()
                                    },
                                    label = {
                                        Text(
                                            text = searchSuggestionStr
                                        )
                                    },
                                    shape = RoundedCornerShape(4.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }
                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        visible = unsplashImages == null,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.search_images_message),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    unsplashImages?.let {
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

val searchSuggestions: List<Int> = listOf(
    R.string.landscape,
    R.string.portrait,
    R.string.nature,
    R.string.architecture,
    R.string.travel,
    R.string.food,
    R.string.animals,
    R.string.abstract_suggestion,
    R.string.technology,
    R.string.fashion
)