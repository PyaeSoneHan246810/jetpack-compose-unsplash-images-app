package com.example.unsplashgallery.presentation.favorites.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.unsplashgallery.R
import com.example.unsplashgallery.presentation.common.components.MainTopAppBar
import com.example.unsplashgallery.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onArrowBackIconButtonClick: () -> Unit,
    onSearchIconButtonClick: () -> Unit,
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .then(modifier),
        topBar = {
            MainTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = stringResource(R.string.favorite_images),
                navigationIconVisible = true,
                onSearchIconButtonClick = onSearchIconButtonClick,
                onArrowBackIconButtonClick = onArrowBackIconButtonClick
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {

        }
    }
}

@Preview
@Composable
private fun FavoritesScreenPrev() {
    AppTheme {
        FavoritesScreen(
            onArrowBackIconButtonClick = {},
            onSearchIconButtonClick = {}
        )
    }
}