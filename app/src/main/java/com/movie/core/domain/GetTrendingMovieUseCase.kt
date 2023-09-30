package com.movie.core.domain

import com.movie.core.data.model.Movie
import com.movie.core.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class GetTrendingMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Flow<List<Movie>> {
        return if (page > 0) movieRepository.getTrending(page)
        else emptyFlow()
    }
}