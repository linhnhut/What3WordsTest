package com.movie.app.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.movie.R
import com.movie.core.data.model.MovieDetail
import com.movie.core.network.retrofit.MovieNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Composable
fun DetailRoute(
    movieId: Long,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            supervisorScope {
                viewModel.getMovie(movieId)
            }
        }
    }
    val movie by viewModel.movie.collectAsStateWithLifecycle()
    movie?.let {
        DetailScreen(movie = it)
    }
}

@Composable
fun DetailScreen(
    movie: MovieDetail
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MovieHeaderImage(MovieNetwork.getPosterPath(movie.backdropPath))
        Column (modifier = Modifier.padding(16.dp)) {
            Text(
                text = movie.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(text = "Genres: ${movie.genres}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Countries: ${movie.productionCountries}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Original Languages: ${movie.originalLanguage}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Spoken Languages: ${movie.spokenLanguages}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Collection: ${movie.belongsToCollection}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Overview: ${movie.overview}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Budget: ${movie.budget}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Popularity: ${movie.popularity}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Production Companies: ${movie.productionCompanies}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Release Date: ${movie.releaseDate}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Revenue: ${movie.revenue}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Runtime: ${movie.runtime}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Status: ${movie.status}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Tagline: ${movie.tagline}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Vote Average: ${movie.voteAverage}", modifier = Modifier.padding(0.dp, 8.dp))
            Text(text = "Vote Count: ${movie.voteCount}", modifier = Modifier.padding(0.dp, 8.dp))
        }
    }
}

@Composable
fun MovieHeaderImage(headerImageUrl: String?) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = headerImageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            // Display a progress bar while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Image(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) {
                imageLoader
            } else {
                painterResource(R.drawable.ic_launcher_background)
            },
            contentDescription = null
        )
    }
}