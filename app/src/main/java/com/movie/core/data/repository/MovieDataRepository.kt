package com.movie.core.data.repository

import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieDataRepository {
    suspend fun getTrending(page: Int): Flow<List<Movie>>
    suspend fun getMovie(id: Long): Flow<MovieDetail>
}