package com.movie.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_detail")
data class MovieDetailEntity(
    @PrimaryKey
    val id: Long,
    val adult: Boolean,
    val backdropPath: String,
    val belongsToCollection: String,
    val budget: Long,
    val genres: String,
    val homepage: String,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Long,
    val spokenLanguages: String,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Long,
)