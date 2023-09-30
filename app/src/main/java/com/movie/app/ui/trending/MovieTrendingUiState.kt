package com.movie.app.ui.trending

import com.movie.core.data.model.Movie

sealed interface MovieTrendingUiState {
    object Loading : MovieTrendingUiState
    data class Success(val movies: List<Movie>): MovieTrendingUiState
}