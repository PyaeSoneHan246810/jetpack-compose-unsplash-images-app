package com.example.unsplashgallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.unsplashgallery.presentation.favorites.screen.FavoritesScreen
import com.example.unsplashgallery.presentation.full_image_display.screen.FullImageDisplayScreen
import com.example.unsplashgallery.presentation.full_image_display.view_model.FullImageDisplayViewModel
import com.example.unsplashgallery.presentation.home.screen.HomeScreen
import com.example.unsplashgallery.presentation.home.view_model.HomeViewModel
import com.example.unsplashgallery.presentation.photographer_profile.screen.PhotographerProfileScreen
import com.example.unsplashgallery.presentation.search.screen.SearchScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Destination.Home
    ) {
        composable<Destination.Home> {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val unsplashImages by homeViewModel.unsplashImages.collectAsState()
            HomeScreen(
                unsplashImages = unsplashImages,
                onImageCardClick = { imageId ->
                    navHostController.navigate(
                        Destination.FullImageDisplay(
                            imageId = imageId
                        )
                    )
                },
                onSearchIconButtonClick = {
                    navHostController.navigate(Destination.Search)
                },
                onFavoritesFabClick = {
                    navHostController.navigate(Destination.Favorites)
                }
            )
        }
        composable<Destination.Search> {
            SearchScreen()
        }
        composable<Destination.Favorites> {
            FavoritesScreen(
                onArrowBackIconButtonClick = {
                    navHostController.navigateUp()
                },
                onSearchIconButtonClick = {
                    navHostController.navigate(Destination.Search)
                }
            )
        }
        composable<Destination.FullImageDisplay> {
            val fullImageDisplayViewModel = hiltViewModel<FullImageDisplayViewModel>()
            val unsplashImage by fullImageDisplayViewModel.unsplashImage.collectAsState()
            FullImageDisplayScreen(
                unsplashImage = unsplashImage,
                onNavigateUp = {
                    navHostController.navigateUp()
                },
                onPhotographerInfoClick = { profileLink ->
                    navHostController.navigate(
                        Destination.PhotographerProfile(
                            profileLink = profileLink
                        )
                    )
                },
                onDownloadOptionClick = { url, title ->
                    fullImageDisplayViewModel.downloadImage(url, title)
                }
            )
        }
        composable<Destination.PhotographerProfile> { navBackStackEntry ->
            val destination = navBackStackEntry.toRoute<Destination.PhotographerProfile>()
            val profileLink = destination.profileLink
            PhotographerProfileScreen(
                profileLink = profileLink,
                onArrowBackIconButtonClick = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}