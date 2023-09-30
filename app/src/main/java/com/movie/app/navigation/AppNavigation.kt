package com.movie.app.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.movie.app.ui.MovieAppState
import com.movie.app.ui.detail.DetailRoute
import com.movie.app.ui.search.SearchRoute
import com.movie.app.ui.trending.TrendingRoute
import java.net.URLEncoder

enum class MovieScreens(val route: String) {
    TrendingScreen("trending"),
    SearchScreen("search"),
    DetailScreen("detail/{movieId}");

    companion object {
        fun fromRoute(route: String?): MovieScreens = when (route?.substringBefore("/")) {
            TrendingScreen.route -> TrendingScreen
            DetailScreen.route -> DetailScreen
            null -> TrendingScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}

@Composable
fun AppNavigation(
    appState: MovieAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = MovieScreens.TrendingScreen.route
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxWidth()
    ) {
        composable(MovieScreens.TrendingScreen.route) {
            appState.showSearchBar.value = true
            TrendingRoute(
                modifier = modifier,
                onMovieClick = {
                    appState.showSearchBar.value = false
                    appState.navController.navigate("detail/$it") {
                        launchSingleTop = true
                    }
                })
        }
        composable(
            route = MovieScreens.SearchScreen.route
        ) {
            SearchRoute(
                modifier = modifier,
                appState = appState,
                onMovieClick = {
                    appState.showSearchBar.value = false
                    appState.navController.navigate("detail/$it") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = MovieScreens.DetailScreen.route,
            arguments = listOf(navArgument("movieId") { type = NavType.LongType })
        ) {
            appState.showSearchBar.value = false
            it.arguments?.getLong("movieId")?.let { id ->
                DetailRoute(movieId = id)
            } ?: kotlin.run {
                LaunchedEffect(true) {
                    onShowSnackbar.invoke("Movie Not Found", null)
                }
            }
        }
    }
}