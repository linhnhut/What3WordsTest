package com.movie.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double
)