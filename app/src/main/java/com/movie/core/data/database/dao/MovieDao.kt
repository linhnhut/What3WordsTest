package com.movie.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.movie.core.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("select * from movies")
    fun getTrendingMovies(): Flow<List<MovieEntity>>

    @Upsert
    suspend fun upsertTrendingMovies(entities: List<MovieEntity>)

    @Query("delete from movies")
    suspend fun clear()
}