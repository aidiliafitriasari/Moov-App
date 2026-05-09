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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val dummyHistory = listOf(
    "Colony",
    "ZonaMerah",
    "AyahIniArahnyaKemanaYa?",
    "TibaTibaSetan"
)

data class DummySearchMovie(
    val title: String,
    val year: String,
    val genre: String,
    val rating: Double,
    val posterColor: Color
)

val dummyRecommended = listOf(
    DummySearchMovie("Komang", "2025", "Drama", 8.2, Color(0xFFFFA500)),
    DummySearchMovie("Selamat Datang!", "2024", "Comedy", 7.8, Color(0xFF32CD32)),
    DummySearchMovie("Sejahtera Limbo", "2025", "Horror", 7.5, Color(0xFF8A2BE2)),
    DummySearchMovie("Lahir Tidak Buka Sebagai Kami", "2024", "Drama", 8.0, Color(0xFF1E90FF))
)

val dummyAllMovies = listOf(
    DummySearchMovie("Komang", "2025", "Drama", 8.2, Color(0xFFFFA500)),
    DummySearchMovie("Colony", "2024", "Sci-Fi", 7.6, Color(0xFF228B22)),
    DummySearchMovie("Zona Merah", "2024", "Horror", 7.3, Color(0xFFE50914)),
    DummySearchMovie("Azzamine", "2024", "Romance", 7.8, Color(0xFFFF69B4)),
    DummySearchMovie("Infinite War", "2025", "Action", 8.7, Color(0xFFE50914)),
    DummySearchMovie("Jumbo", "2025", "Animation", 8.9, Color(0xFF4169E1)),
    DummySearchMovie("Sekawan Limo", "2024", "Comedy", 8.0, Color(0xFF8A2BE2)),
    DummySearchMovie("Transformers 4", "2024", "Action", 7.5, Color(0xFF1E90FF))
)

@Composable
fun SearchScreen(onMovieClick: (DummySearchMovie) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchHistory by remember { mutableStateOf(dummyHistory) }
    var searchResults by remember { mutableStateOf<List<DummySearchMovie>>(emptyList()) }

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
                searchResults = if (query.isNotEmpty()) {
                    dummyAllMovies.filter { it.title.contains(query, ignoreCase = true) }
                } else {
                    emptyList()
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
            // Hasil Pencarian
            Text("Search Results", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (searchResults.isEmpty()) {
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
                                searchResults = dummyAllMovies.filter {
                                    it.title.contains(historyItem, ignoreCase = true)
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

            // Recommended
            Text("Recommended For You", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                for (i in dummyRecommended.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).padding(end = 6.dp).height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.verticalGradient(colors = listOf(dummyRecommended[i].posterColor, dummyRecommended[i].posterColor.copy(alpha = 0.5f)))),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(dummyRecommended[i].title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Text("${dummyRecommended[i].year} • ${dummyRecommended[i].genre}", color = Color(0xFFB3B3B3), fontSize = 11.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                                    Text(" ${dummyRecommended[i].rating}", color = Color(0xFFFFD700), fontSize = 12.sp)
                                }
                            }
                        }
                        if (i + 1 < dummyRecommended.size) {
                            Box(
                                modifier = Modifier.weight(1f).padding(start = 6.dp).height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.verticalGradient(colors = listOf(dummyRecommended[i + 1].posterColor, dummyRecommended[i + 1].posterColor.copy(alpha = 0.5f)))),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(dummyRecommended[i + 1].title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("${dummyRecommended[i + 1].year} • ${dummyRecommended[i + 1].genre}", color = Color(0xFFB3B3B3), fontSize = 11.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                                        Text(" ${dummyRecommended[i + 1].rating}", color = Color(0xFFFFD700), fontSize = 12.sp)
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
fun SearchMovieItem(movie: DummySearchMovie, onClick: () -> Unit) {
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
                .background(Brush.verticalGradient(colors = listOf(movie.posterColor, movie.posterColor.copy(alpha = 0.6f)))),
            contentAlignment = Alignment.Center
        ) {
            Text(movie.title.first().toString(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(movie.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${movie.year} • ${movie.genre}", color = Color(0xFFB3B3B3), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                Text(" ${movie.rating}", color = Color(0xFFFFD700), fontSize = 12.sp)
            }
        }
    }
}