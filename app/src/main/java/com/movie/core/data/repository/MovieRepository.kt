package com.movie.core.data.repository

import android.util.Log
import com.movie.core.common.AppDispatchers
import com.movie.core.common.Dispatcher
import com.movie.core.common.di.ApplicationScope
import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import com.movie.core.datasource.MovieLocalDataSource
import com.movie.core.datasource.MovieRemoteDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val localDataSource: MovieLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource
) : MovieDataRepository {

    private var cacheTrendingMovie: List<Movie> = emptyList()

    override suspend fun getTrending(page: Int): Flow<List<Movie>> {

        val remoteJob = CoroutineScope(Dispatchers.IO).async {
            remoteDataSource.getTrending(page)
        }

        return try {
            localDataSource.getTrending().combine(remoteJob.await()) { f1, f2 ->
                val list = mutableListOf<Movie>()
                list.addAll(f1)
                list.addAll(f2)
                val rs = list.distinct()

                withContext(Dispatchers.Default) {
                    localDataSource.upsertMovies(rs)
                }

                cacheTrendingMovie = rs
                list
            }
        } catch (e: Exception) {
            localDataSource.getTrending()
        }
    }

    override suspend fun getMovie(id: Long): Flow<MovieDetail> {

        return localDataSource.getMovie(id)?.let {
            flowOf(it)
        } ?: kotlin.run {
            val remote = try {
                remoteDataSource.getMovie(id)
            } catch (e: Exception) {
                null
            }
            remote?.let {
            localDataSource.upsertMovie(it)
                flowOf(it)
            } ?: flowOf()
        }
    }
}