package com.example.unsplashgallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.unsplashgallery.presentation.favorites.screen.FavoritesScreen
import com.example.unsplashgallery.presentation.favorites.view_model.FavoritesViewModel
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
            val unsplashImages by homeViewModel.unsplashImages.collectAsStateWithLifecycle()
            val favoriteImagesIds by homeViewModel.favoriteImagesIds.collectAsStateWithLifecycle()
            val snackBarEventFlow = homeViewModel.snackBarEvent
            HomeScreen(
                unsplashImages = unsplashImages?.collectAsLazyPagingItems(),
                favoriteImagesIds = favoriteImagesIds,
                snackBarEventFlow = snackBarEventFlow,
                onImageCardClick = { imageId ->
                    navHostController.navigate(
                        Destination.FullImageDisplay(
                            imageId = imageId
                        )
                    )
                },
                onToggleFavoriteStatus = { image ->
                    homeViewModel.toggleFavoriteStatus(
                        image = image
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
            val searchScreenState by searchViewModel.searchScreenState.collectAsStateWithLifecycle()
            val favoriteImagesIds by searchViewModel.favoriteImagesIds.collectAsStateWithLifecycle()
            val snackBarEventFlow = searchViewModel.snackBarEvent
            SearchScreen(
                searchQuery = searchScreenState.searchQuery,
                unsplashImages = searchScreenState.searchResults?.collectAsLazyPagingItems(),
                favoriteImagesIds = favoriteImagesIds,
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
                onToggleFavoriteStatus = { image ->
                    searchViewModel.toggleFavoriteStatus(
                        image = image
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
            val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
            val favoriteImagesId by favoritesViewModel.favoriteImagesIds.collectAsStateWithLifecycle()
            val favoriteImages by favoritesViewModel.favoriteImages.collectAsStateWithLifecycle()
            val snackBarEventFlow = favoritesViewModel.snackBarEvent
            FavoritesScreen(
                unsplashImages = favoriteImages?.collectAsLazyPagingItems(),
                favoriteImagesIds = favoriteImagesId,
                snackBarEventFlow = snackBarEventFlow,
                onImageCardClick = { imageId ->
                    navHostController.navigate(
                        Destination.FullImageDisplay(
                            imageId = imageId
                        )
                    )
                },
                onToggleFavoriteStatus = { image ->
                    favoritesViewModel.toggleFavoriteStatus(
                        image = image
                    )
                },
                onSearchIconButtonClick = {
                    navHostController.navigate(Destination.Search)
                },
                onArrowBackIconButtonClick = {
                    navHostController.navigateUp()
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
            val setWallpaperViewModel = hiltViewModel<SetWallpaperViewModel>()
            SetWallpaperScreen(
                imageUrl = destination.imageUrl,
                isLoading = setWallpaperViewModel.isLoading,
                snackBarEventFlow = setWallpaperViewModel.snackBarEvent,
                onConfirmClick = { url ->
                    setWallpaperViewModel.setImageWallpaper(url)
                }
            )
        }
        composable<Destination.PhotographerProfile> { navBackStackEntry ->
            val destination = navBackStackEntry.toRoute<Destination.PhotographerProfile>()
            PhotographerProfileScreen(
                profileLink = destination.profileLink,
                onArrowBackIconButtonClick = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}