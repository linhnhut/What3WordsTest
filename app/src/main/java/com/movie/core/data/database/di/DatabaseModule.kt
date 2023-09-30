package com.movie.core.data.database.di

import android.content.Context
import androidx.room.Room
import com.movie.core.data.database.MovieDatabase
import com.movie.core.data.database.dao.MovieDao
import com.movie.core.data.database.dao.MovieDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMovieDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        "movie-database",
    ).build()

    @Provides
    fun providesMoviesDao(
        database: MovieDatabase,
    ): MovieDao = database.movieDao()

    @Provides
    fun providesMovieDetailDao(
        database: MovieDatabase,
    ): MovieDetailDao = database.movieDetailDao()
}