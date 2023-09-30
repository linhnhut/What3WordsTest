package com.movie.core.data.model

import kotlinx.serialization.SerialName

data class Movie(
    val id: Long,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double
)