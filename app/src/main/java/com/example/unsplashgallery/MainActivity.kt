package com.example.unsplashgallery

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unsplashgallery.domain.repository.NetworkConnectivityManager
import com.example.unsplashgallery.presentation.common.components.NetworkStatusBar
import com.example.unsplashgallery.presentation.navigation.MainNavGraph
import com.example.unsplashgallery.presentation.theme.AppTheme
import com.example.unsplashgallery.presentation.theme.green
import com.example.unsplashgallery.presentation.theme.red
import com.example.unsplashgallery.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private lateinit var navHostController: NavHostController

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    @SuppressLint("SourceLockedOrientationActivity", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            navHostController = rememberNavController()
            val networkStatus by networkConnectivityManager.networkStatus.collectAsState()
            var isNetworkStatusBarVisible by rememberSaveable {
                mutableStateOf(false)
            }
            var networkStatusBarBackgroundColor by remember {
                mutableStateOf(Color.Black)
            }
            var networkStatusBarMessage by rememberSaveable {
                mutableStateOf("")
            }
            val connectedMessage = stringResource(R.string.connected)
            val disconnectedMessage = stringResource(R.string.disconnected)
            LaunchedEffect(key1 = networkStatus) {
                when(networkStatus) {
                    NetworkStatus.Connected -> {
                        isNetworkStatusBarVisible = true
                        networkStatusBarBackgroundColor = green
                        networkStatusBarMessage = connectedMessage
                    }
                    NetworkStatus.Disconnected -> {
                        isNetworkStatusBarVisible = true
                        networkStatusBarBackgroundColor = red
                        networkStatusBarMessage = disconnectedMessage
                    }
                }
                if (networkStatus == NetworkStatus.Connected) {
                    delay(2000)
                    isNetworkStatusBarVisible = false
                }
            }
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    snackbarHost = {
                        NetworkStatusBar(
                            isVisible = isNetworkStatusBarVisible,
                            backgroundColor = networkStatusBarBackgroundColor,
                            message = networkStatusBarMessage,
                        )
                    }
                ) {
                    MainNavGraph(
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}