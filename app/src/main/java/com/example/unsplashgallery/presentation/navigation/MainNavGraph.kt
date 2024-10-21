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
import com.example.unsplashgallery.presentation.favorites.view_model.FavoritesViewModel
import com.example.unsplashgallery.presentation.full_image_display.screen.FullImageDisplayScreen
import com.example.unsplashgallery.presentation.full_image_display.view_model.FullImageDisplayViewModel
import com.example.unsplashgallery.presentation.home.screen.HomeScreen
import com.example.unsplashgallery.presentation.home.view_model.HomeViewModel
import com.example.unsplashgallery.presentation.photographer_profile.screen.PhotographerProfileScreen
import com.example.unsplashgallery.presentation.search.screen.SearchScreen
import com.example.unsplashgallery.presentation.search.view_model.SearchViewModel

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
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen()
        }
        composable<Destination.Favorites> {
            val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
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
                onArrowBackIconButtonClick = {
                    navHostController.navigateUp()
                },
                onDownloadIconButtonClick = {},
                onPhotographerInfoClick = { profileLink ->
                    navHostController.navigate(
                        Destination.PhotographerProfile(
                            profileLink = profileLink
                        )
                    )
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