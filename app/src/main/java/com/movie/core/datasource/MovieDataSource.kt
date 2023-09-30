package com.movie.core.datasource

import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {
    suspend fun getTrending(page: Int = 1): Flow<List<Movie>>
    suspend fun getMovie(id: Long): MovieDetail?
}