package com.moov.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moov.app.data.remote.TmdbMovieDto
import com.moov.app.data.repository.MovieRepository
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(onMovieClick: (TmdbMovieDto) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchResults by remember { mutableStateOf<List<TmdbMovieDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val repository = remember { MovieRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                isSearching = query.isNotEmpty()
                if (query.isNotEmpty()) {
                    isLoading = true
                    kotlinx.coroutines.MainScope().launch {
                        try {
                            val response = repository.searchMovies(query)
                            searchResults = response.results
                        } catch (e: Exception) {
                            searchResults = emptyList()
                        }
                        isLoading = false
                    }
                } else {
                    searchResults = emptyList()
                }
            },
            placeholder = { Text("Search movies, series...", color = Color(0xFF757575)) },
            leadingIcon = {
                Icon(Icons.Filled.Search, "Search", tint = Color(0xFF757575))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        isSearching = false
                        searchResults = emptyList()
                    }) {
                        Icon(Icons.Filled.Clear, "Clear", tint = Color(0xFF757575))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFE50914), unfocusedBorderColor = Color(0xFF2E2E2E),
                focusedContainerColor = Color(0xFF1A1A1A), unfocusedContainerColor = Color(0xFF1A1A1A)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isSearching) {
            // Loading
            if (isLoading) {
                Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE50914))
                }
            }
            // Hasil Pencarian
            Text("Search Results", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (!isLoading && searchResults.isEmpty()) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("No results found", color = Color(0xFF757575), fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                searchResults.forEach { movie ->
                    SearchMovieItem(movie = movie, onClick = { onMovieClick(movie) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            // History
            if (searchHistory.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("History", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { searchHistory = emptyList() }) {
                        Text("Clear All", color = Color(0xFFE50914), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                searchHistory.forEach { historyItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchQuery = historyItem
                                isSearching = true
                                isLoading = true
                                kotlinx.coroutines.MainScope().launch {
                                    try {
                                        val response = repository.searchMovies(historyItem)
                                        searchResults = response.results
                                    } catch (e: Exception) {
                                        searchResults = emptyList()
                                    }
                                    isLoading = false
                                }
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Search, null, tint = Color(0xFF757575), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(historyItem, color = Color(0xFFB3B3B3), fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trending Movies
            Text("Trending Movies", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            var trendingMovies by remember { mutableStateOf<List<TmdbMovieDto>>(emptyList()) }
            LaunchedEffect(Unit) {
                try {
                    trendingMovies = repository.getTrendingMovies().results.take(4)
                } catch (_: Exception) { }
            }

            Column {
                for (i in trendingMovies.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        // Box Kiri
                        Box(
                            modifier = Modifier.weight(1f).padding(end = 6.dp).height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.DarkGray)
                                .clickable { onMovieClick(trendingMovies[i]) },
                            contentAlignment = Alignment.BottomStart
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w500${trendingMovies[i].poster_path}",
                                contentDescription = trendingMovies[i].title,
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(trendingMovies[i].title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Text("${trendingMovies[i].release_date?.take(4) ?: "N/A"}", color = Color(0xFFB3B3B3), fontSize = 11.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                                    Text(" ${"%.1f".format(trendingMovies[i].vote_average)}", color = Color(0xFFFFD700), fontSize = 12.sp)
                                }
                            }
                        }
                        // Box Kanan
                        if (i + 1 < trendingMovies.size) {
                            Box(
                                modifier = Modifier.weight(1f).padding(start = 6.dp).height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.DarkGray)
                                    .clickable { onMovieClick(trendingMovies[i + 1]) },
                                contentAlignment = Alignment.BottomStart
                            ) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w500${trendingMovies[i + 1].poster_path}",
                                    contentDescription = trendingMovies[i + 1].title,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(trendingMovies[i + 1].title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("${trendingMovies[i + 1].release_date?.take(4) ?: "N/A"}", color = Color(0xFFB3B3B3), fontSize = 11.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                                        Text(" ${"%.1f".format(trendingMovies[i + 1].vote_average)}", color = Color(0xFFFFD700), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SearchMovieItem(movie: TmdbMovieDto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(50.dp).height(70.dp).clip(RoundedCornerShape(6.dp))
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(movie.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${movie.release_date?.take(4) ?: "N/A"}", color = Color(0xFFB3B3B3), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                Text(" ${"%.1f".format(movie.vote_average)}", color = Color(0xFFFFD700), fontSize = 12.sp)
            }
        }
    }
}