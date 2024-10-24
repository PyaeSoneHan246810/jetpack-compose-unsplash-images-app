package com.example.unsplashgallery.presentation.photographer_profile.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.unsplashgallery.R
import com.example.unsplashgallery.presentation.common.components.AnimatedLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotographerProfileScreen(
    modifier: Modifier = Modifier,
    profileLink: String,
    onArrowBackIconButtonClick: () -> Unit
) {
    var isWebPageLoading by rememberSaveable {
        mutableStateOf(true)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onArrowBackIconButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AndroidView(
                    modifier = Modifier
                        .matchParentSize(),
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isWebPageLoading = false
                                }
                            }
                            loadUrl(profileLink)
                        }
                    },
                )
                if (isWebPageLoading) {
                    AnimatedLoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}