package com.movie.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.movie.core.data.database.model.MovieDetailEntity

@Dao
interface MovieDetailDao {

    @Query("select * from movie_detail where id = :id")
    fun getMovie(id: Long): MovieDetailEntity?

    @Upsert
    suspend fun upsertMovie(entity: MovieDetailEntity)
}