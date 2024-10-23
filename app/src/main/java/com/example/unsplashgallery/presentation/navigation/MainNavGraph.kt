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
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.unsplashgallery.presentation.favorites.screen.FavoritesScreen
import com.example.unsplashgallery.presentation.full_image_display.screen.FullImageDisplayScreen
import com.example.unsplashgallery.presentation.full_image_display.view_model.FullImageDisplayViewModel
import com.example.unsplashgallery.presentation.home.screen.HomeScreen
import com.example.unsplashgallery.presentation.home.view_model.HomeViewModel
import com.example.unsplashgallery.presentation.photographer_profile.screen.PhotographerProfileScreen
import com.example.unsplashgallery.presentation.search.screen.SearchScreen
import com.example.unsplashgallery.presentation.search.view_model.SearchViewModel
import com.example.unsplashgallery.presentation.set_wallpaper.screen.SetWallpaperScreen
import com.example.unsplashgallery.presentation.set_wallpaper.view_model.SetWallpaperViewModel

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
            val snackBarEventFlow = homeViewModel.snackBarEvent
            HomeScreen(
                unsplashImages = unsplashImages,
                snackBarEventFlow = snackBarEventFlow,
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
            val searchScreenState by searchViewModel.searchScreenState.collectAsState()
            val unsplashImages = searchScreenState.searchResults?.collectAsLazyPagingItems()
            val snackBarEventFlow = searchViewModel.snackBarEvent
            SearchScreen(
                searchQuery = searchScreenState.searchQuery,
                unsplashImages = unsplashImages,
                snackBarEventFlow = snackBarEventFlow,
                onSearchQueryValueChanged = { newQuery ->
                    searchViewModel.updateSearchQuery(
                        newQuery = newQuery
                    )
                },
                onSearch = {
                    searchViewModel.getSearchImages()
                },
                onImageCardClick = { imageId ->
                    navHostController.navigate(
                        Destination.FullImageDisplay(
                            imageId = imageId
                        )
                    )
                },
                onFavoritesFabClick = {
                    navHostController.navigate(Destination.Favorites)
                },
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
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
            val snackBarEventFlow = fullImageDisplayViewModel.snackBarEvent
            FullImageDisplayScreen(
                unsplashImage = unsplashImage,
                snackBarEventFlow = snackBarEventFlow,
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
                },
                onSetWallpaperButtonClick = { url ->
                    navHostController.navigate(
                        Destination.SetWallpaper(
                            imageUrl = url
                        )
                    )
                }
            )
        }
        composable<Destination.SetWallpaper> { navBackStackEntry ->
            val destination = navBackStackEntry.toRoute<Destination.SetWallpaper>()
            val imageUrl = destination.imageUrl
            val setWallpaperViewModel = hiltViewModel<SetWallpaperViewModel>()
            SetWallpaperScreen(
                imageUrl = imageUrl,
                onConfirmClick = { url ->
                    setWallpaperViewModel.setImageWallpaper(url)
                    navHostController.navigateUp()
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