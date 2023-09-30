package com.movie.core.data

import com.movie.core.data.database.model.MovieDetailEntity
import com.movie.core.data.database.model.MovieEntity
import com.movie.core.data.model.Movie
import com.movie.core.data.model.MovieDetail
import com.movie.core.network.model.NetworkMovieDetail
import com.movie.core.network.model.NetworkMoviePageSearch
import com.movie.core.network.model.NetworkMovies
import com.movie.core.network.retrofit.MovieNetwork


fun NetworkMovies.getMovies(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            posterPath = it.posterPath ?: it.backdropPath ?: "",
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage
        )
    }
}

fun NetworkMoviePageSearch.getMovies(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            posterPath = it.posterPath ?: it.backdropPath ?: "",
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage
        )
    }
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage
    )
}

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage
    )
}

fun MovieDetailEntity.toMovieDetail() : MovieDetail {
    return MovieDetail(
        adult = this.adult,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection,
        budget = budget,
        genres = genres,
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        productionCountries = productionCountries,
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}

fun MovieDetail.toMovieDetail() : MovieDetailEntity {
    return MovieDetailEntity(
        adult = this.adult,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection,
        budget = budget,
        genres = genres,
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        productionCountries = productionCountries,
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}

fun NetworkMovieDetail.toMovieDetail() : MovieDetail {
    return MovieDetail(
        adult = this.adult ?: false,
        backdropPath = this.backdropPath ?: posterPath ?: "",
        belongsToCollection = this.belongsToCollection?.name ?: "",
        budget = budget,
        genres = genres.joinToString { it.name },
        homepage = homepage,
        id = id,
        imdbId = imdbId ?: "",
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath ?: backdropPath ?: "",
        productionCompanies = productionCompanies.joinToString { it.name },
        productionCountries = productionCountries.joinToString { it.name },
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages.joinToString { it.englishName },
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}