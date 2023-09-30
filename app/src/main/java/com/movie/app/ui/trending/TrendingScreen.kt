package com.movie.app.ui.trending

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.movie.R
import com.movie.app.ui.utils.OnBottomReached
import com.movie.core.data.model.Movie
import com.movie.core.network.retrofit.MovieNetwork
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Composable
fun TrendingRoute(
    modifier: Modifier = Modifier,
    viewModel: TrendingViewModel = hiltViewModel(),
    onMovieClick: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            supervisorScope {
                viewModel.getTrendingState()
            }
        }
    }
    val trendingState by viewModel.trendingState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    listState.OnBottomReached {
        if (trendingState is MovieTrendingUiState.Success) {
            if ((trendingState as MovieTrendingUiState.Success).movies.isNotEmpty()) {
                Log.d("TAG", "OnBottomReached")
                scope.launch {
                    supervisorScope {
                        viewModel.getTrendingState()
                    }
                }
            }
        }
    }

    TrendingScreen(
        onMovieClick = onMovieClick,
        trendingState = trendingState,
        modifier = modifier,
        listState = listState
    )
}

@Composable
fun TrendingScreen(
    onMovieClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    trendingState: MovieTrendingUiState,
    listState: LazyListState = rememberLazyListState()
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState
        ) {
            item {
                Text(
                    text = "Trending Movies",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
            when (trendingState) {
                MovieTrendingUiState.Loading -> Unit
                is MovieTrendingUiState.Success -> {
                    items(
                        items = trendingState.movies,
                        key = { it.id },
                        contentType = { "movieItem" }
                    ) { movie ->
                        MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row {
            MovieHeaderImage(MovieNetwork.getPosterPath(movie.posterPath))

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = movie.title,
                    Modifier.padding(8.dp, 16.dp, 8.dp, 4.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.year) + movie.releaseDate.split('-')[0],
                    Modifier.padding(8.dp, 0.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = stringResource(id = R.string.score) + String.format("%.1f", movie.voteAverage),
                    Modifier.padding(8.dp, 4.dp, 8.dp, 0.dp),
                    fontSize = 14.sp,
                )

            }
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
            .width(80.dp)
            .height(120.dp),
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
                .fillMaxWidth()
                .height(120.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row {
            MovieHeaderImage("")
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Title",
                    Modifier.padding(8.dp, 16.dp, 8.dp, 4.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Year",
                    Modifier.padding(8.dp, 0.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Vote",
                    Modifier.padding(8.dp, 4.dp, 8.dp, 0.dp),
                    fontSize = 14.sp,
                )

            }
        }
    }
}