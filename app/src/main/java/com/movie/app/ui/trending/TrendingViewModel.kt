package com.movie.app.ui.trending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.core.domain.GetTrendingMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingMovieUseCase: GetTrendingMovieUseCase
) : ViewModel() {

    private var pageNum = 1;
    private var totalPageNum = 100;

    private val _trendingState: MutableStateFlow<MovieTrendingUiState> =
        MutableStateFlow(MovieTrendingUiState.Loading)
    val trendingState: StateFlow<MovieTrendingUiState> = _trendingState

    suspend fun getTrendingState() {
        val page = if (pageNum < totalPageNum) pageNum ++ else -1

        Log.d("TAG", "getTrendingMovieUseCase - page=$page")
        getTrendingMovieUseCase(page)
            .map(MovieTrendingUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MovieTrendingUiState.Loading
            )
            .collectLatest {
                Log.d("TAG", "getTrendingState: ${it::class.java}")
                _trendingState.value = it
            }
    }
}