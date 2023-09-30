package com.movie.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movie.core.data.database.dao.MovieDao
import com.movie.core.data.database.dao.MovieDetailDao
import com.movie.core.data.database.model.MovieDetailEntity
import com.movie.core.data.database.model.MovieEntity

@Database(version = 1, entities = [MovieEntity::class, MovieDetailEntity::class])
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
}