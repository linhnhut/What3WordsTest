package com.movie.app.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movie.R
import com.movie.app.ui.MovieAppState
import com.movie.app.ui.theme.MovieTheme
import com.movie.app.ui.trending.MovieCard
import com.movie.app.ui.trending.MovieTrendingUiState
import com.movie.app.ui.trending.TrendingScreen
import com.movie.app.ui.utils.OnBottomReached
import kotlinx.coroutines.launch

@Composable
fun SearchRoute(
    appState: MovieAppState,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Long) -> Unit
) {

    val searchQuery by remember { appState.searchQuery }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getSearchState(appState.searchQuery.value)
        }
    }
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    SearchScreen(
        onMovieClick = onMovieClick,
        searchState = searchState,
        modifier = modifier,
        listState = listState
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchState: SearchUiState,
    onMovieClick: (Long) -> Unit = {},
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
                    text = "Search Results",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
            when (searchState) {
                SearchUiState.Loading -> Unit
                is SearchUiState.Success -> {
                    items(
                        items = searchState.movies,
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


@Composable
fun SearchToolbar(
    modifier: Modifier,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            searchQuery = searchQuery,
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
    }

    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(
                    id = R.string.search,
                ),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(
                            id = R.string.clear_search_text,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if (!it.contains("\n")) {
                onSearchQueryChanged(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("searchTextField")
            .padding(0.dp),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle.Default.copy(
            fontSize = 12.sp
        )
    )
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    MovieTheme {
        SearchToolbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp, 8.dp, 8.dp),
            onSearchQueryChanged = {}
        )
    }
}