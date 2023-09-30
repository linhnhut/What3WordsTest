package com.movie.app.ui.search

import com.movie.core.data.model.Movie

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Success(val movies: List<Movie>): SearchUiState
}