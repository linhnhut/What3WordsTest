package com.movie.app

import androidx.lifecycle.ViewModel
import com.movie.core.data.repository.MovieDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    movieDataRepository: MovieDataRepository
) : ViewModel() {
//    val uiState: StateFlow<MainActivityUiState> = movieDataRepository.movieData.map {
//        MainActivityUiState.Success(it)
//    }.stateIn(
//        scope = viewModelScope,
//        initialValue = MainActivityUiState.Loading,
//        started = SharingStarted.WhileSubscribed(5000)
//    )
}
