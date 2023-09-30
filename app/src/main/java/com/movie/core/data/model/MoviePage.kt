package com.movie.core.data.model

data class MoviePage(
    val page: Long,
    val movies: List<Movie>,
    val totalPages: Long
)