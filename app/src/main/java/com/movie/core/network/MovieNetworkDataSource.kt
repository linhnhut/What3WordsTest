package com.movie.core.network

import com.movie.core.network.model.NetworkMovieDetail
import com.movie.core.network.model.NetworkMoviePageSearch
import com.movie.core.network.model.NetworkMovies

interface MovieNetworkDataSource {
    suspend fun getTrending(page: Int): NetworkMovies
    suspend fun search(query: String): NetworkMoviePageSearch
    suspend fun getMovie(id: Long): NetworkMovieDetail
}