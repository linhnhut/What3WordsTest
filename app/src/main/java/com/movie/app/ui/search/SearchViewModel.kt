package com.movie.app.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.core.datasource.MovieRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource
) : ViewModel() {

    private val _searchState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.Loading)
    val searchState: StateFlow<SearchUiState> = _searchState

    suspend fun getSearchState(query: String) {
        Log.d("TAG", "getTrendingState")
        try {
            remoteDataSource.search(query)
                .map(SearchUiState::Success)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SearchUiState.Loading
                )
                .collectLatest {
                    Log.d("TAG", "getSearchState: ${it::class.java}")
                    _searchState.value = it
                }
        } catch (_ : Exception ) {}

    }
}