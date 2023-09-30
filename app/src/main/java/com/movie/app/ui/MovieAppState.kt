package com.movie.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.movie.core.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberMovieAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): MovieAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
    ) {
        MovieAppState(navController, coroutineScope, networkMonitor)
    }
}

@Stable
class MovieAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    var showSearchBar: MutableState<Boolean> = mutableStateOf(true)
    var searchQuery: MutableState<String> = mutableStateOf("")
}