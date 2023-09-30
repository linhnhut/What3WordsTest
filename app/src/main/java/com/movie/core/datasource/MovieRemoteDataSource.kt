package com.movie.core.datasource

import com.movie.core.common.AppDispatchers
import com.movie.core.common.Dispatcher
import com.movie.core.data.getMovies
import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import com.movie.core.data.toMovieDetail
import com.movie.core.network.retrofit.MovieNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: MovieNetwork
) : MovieDataSource {
    override suspend fun getTrending(page: Int): Flow<List<Movie>> {
        return withContext(ioDispatcher) {
            flowOf(network.getTrending(page).getMovies())
        }
    }

    override suspend fun getMovie(id: Long): MovieDetail {
        return withContext(ioDispatcher) {
            network.getMovie(id).toMovieDetail()
        }
    }

    suspend fun search(query: String) : Flow<List<Movie>> {
        return withContext(ioDispatcher) {
            flowOf(network.search(query).getMovies())
        }
    }
}