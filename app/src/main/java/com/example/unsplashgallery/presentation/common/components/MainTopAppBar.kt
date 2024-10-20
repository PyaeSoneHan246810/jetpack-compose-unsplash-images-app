package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.unsplashgallery.R
import com.example.unsplashgallery.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = stringResource(R.string.app_name),
    navigationIconVisible: Boolean = false,
    onArrowBackIconButtonClick: () -> Unit = {},
    onSearchIconButtonClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (navigationIconVisible)
                IconButton(
                    onClick = onArrowBackIconButtonClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
        },
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(
                            text = title.split(" ").first()
                        )
                    }
                    append(
                        text = " "
                    )
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        append(
                            text = title.split(" ").last()
                        )
                    }
                },
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(
                onClick = onSearchIconButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeTopAppBarPrev() {
    AppTheme {
        MainTopAppBar(
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onSearchIconButtonClick = {},
        )
    }
}