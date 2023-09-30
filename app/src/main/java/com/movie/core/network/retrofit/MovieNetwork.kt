package com.movie.core.network.retrofit

import android.net.Uri
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.movie.core.network.MovieNetworkDataSource
import com.movie.core.network.model.NetworkMovieDetail
import com.movie.core.network.model.NetworkMoviePageSearch
import com.movie.core.network.model.NetworkMovieSearch
import com.movie.core.network.model.NetworkMovies
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface MovieNetworkApi {
    companion object {
        private const val KEY = "47aa75b56464da7a186b813a50035cd4"
    }

    @GET(value = "trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): NetworkMovies

    @GET(value = "search/movie")
    suspend fun search(
        @Query("api_key") apiKey: String = KEY,
        @Query("query") query: String
    ): NetworkMoviePageSearch

    @GET(value = "movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String = KEY
    ): NetworkMovieDetail

}

@Singleton
class MovieNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : MovieNetworkDataSource {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val POSTER_URL = "https://image.tmdb.org/t/p/w500"

        fun getPosterPath(path: String) = POSTER_URL + path
    }

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(MovieNetworkApi::class.java)

    override suspend fun getTrending(page: Int): NetworkMovies {
        return networkApi.getTrending(page = page)
    }

    override suspend fun search(query: String): NetworkMoviePageSearch {
        return networkApi.search(query = query)
    }

    override suspend fun getMovie(id: Long): NetworkMovieDetail {
        return networkApi.getMovie(id)
    }

}