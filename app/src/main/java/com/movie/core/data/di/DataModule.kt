package com.movie.core.data.di

import com.movie.core.data.repository.MovieDataRepository
import com.movie.core.data.repository.MovieRepository
import com.movie.core.datasource.MovieLocalDataSource
import com.movie.core.datasource.MovieRemoteDataSource
import com.movie.core.utils.ConnectivityManagerNetworkMonitor
import com.movie.core.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsMovieDataRepository(
        movieRepository: MovieRepository
    ): MovieDataRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}