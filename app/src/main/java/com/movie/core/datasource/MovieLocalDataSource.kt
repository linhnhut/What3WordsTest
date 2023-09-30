package com.movie.core.datasource

import com.movie.core.common.AppDispatchers
import com.movie.core.common.Dispatcher
import com.movie.core.data.database.dao.MovieDao
import com.movie.core.data.database.dao.MovieDetailDao
import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import com.movie.core.data.toMovie
import com.movie.core.data.toMovieDetail
import com.movie.core.data.toMovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao
) : MovieDataSource {

    override suspend fun getTrending(page: Int): Flow<List<Movie>> =
        movieDao.getTrendingMovies().map { it.map { m -> m.toMovie() } }

    override suspend fun getMovie(id: Long): MovieDetail? {
        return movieDetailDao.getMovie(id)?.toMovieDetail()
    }

    suspend fun upsertMovies(movie: List<Movie>) {
        movieDao.upsertTrendingMovies(movie.map { it.toMovieEntity() })
    }

    suspend fun upsertMovie(movie: MovieDetail) {
        movieDetailDao.upsertMovie(movie.toMovieDetail())
    }
}