package com.moov.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

// Data dummy film
data class DummyMovie(
    val title: String,
    val genre: String,
    val rating: Double,
    val posterColor: Color
)

val dummyTrending = listOf(
    DummyMovie("Infinite War", "Action", 8.7, Color(0xFFE50914)),
    DummyMovie("Transformers 4", "Action", 7.5, Color(0xFF1E90FF)),
    DummyMovie("Komang", "Comedy", 8.2, Color(0xFFFFA500)),
    DummyMovie("Azzamine", "Romance", 7.8, Color(0xFFFF69B4)),
    DummyMovie("Maabilu", "Drama", 8.0, Color(0xFF32CD32))
)

val dummyPopular = listOf(
    DummyMovie("Sekawan Limo", "Comedy", 8.0, Color(0xFF8A2BE2)),
    DummyMovie("Janur Irong", "Horror", 7.3, Color(0xFF2F4F4F)),
    DummyMovie("Jumbo", "Animation", 8.9, Color(0xFF4169E1)),
    DummyMovie("Colony", "Sci-Fi", 7.6, Color(0xFF228B22)),
    DummyMovie("Komang", "Comedy", 8.2, Color(0xFFFFA500)),
    DummyMovie("Azzamine", "Romance", 7.8, Color(0xFFFF69B4)),
    DummyMovie("Infinite War", "Action", 8.7, Color(0xFFE50914)),
    DummyMovie("Maabilu", "Drama", 8.0, Color(0xFF32CD32))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ========== HEADER: Foto Profil + Welcome ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto Profil
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE50914)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Welcome,",
                    color = Color(0xFFB3B3B3),
                    fontSize = 14.sp
                )
                Text(
                    text = "Aidilia Fitriasari",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ========== TRENDING NOW - 1 Poster + Indikator Titik ==========
        Text(
            text = "Trending Now",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        var trendingIndex by remember { mutableIntStateOf(0) }
        val scrollState = rememberScrollState()

        // Sync index dengan scroll
        LaunchedEffect(scrollState.value) {
            val itemWidth = 340 // lebar poster + padding
            trendingIndex = (scrollState.value / itemWidth).coerceIn(0, dummyTrending.size - 1)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.horizontalScroll(scrollState)
            ) {
                dummyTrending.forEach { movie ->
                    Box(
                        modifier = Modifier
                            .width(328.dp) // hampir full layar
                            .height(180.dp)
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(movie.posterColor, movie.posterColor.copy(alpha = 0.6f))
                                )
                            ),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        // Overlay gelap
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = movie.title,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "⭐ ${movie.rating}",
                                    color = Color(0xFFFFD700),
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = movie.genre,
                                    color = Color(0xFFB3B3B3),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Indikator Titik
            Row(horizontalArrangement = Arrangement.Center) {
                dummyTrending.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == trendingIndex) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == trendingIndex) Color(0xFFE50914) else Color(0xFF757575)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ========== GENRE CHIPS ==========
        Text(
            text = "Genre",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        val genres = listOf("All", "Action", "Comedy", "Romance", "Horror")
        var selectedGenre by remember { mutableStateOf("All") }

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            genres.forEach { genre ->
                FilterChip(
                    selected = selectedGenre == genre,
                    onClick = { selectedGenre = genre },
                    label = {
                        Text(
                            text = genre,
                            color = if (selectedGenre == genre) Color.White else Color(0xFFB3B3B3)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE50914),
                        containerColor = Color(0xFF1A1A1A)
                    ),
                    modifier = Modifier.padding(end = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ========== POPULAR THIS WEEK - 4 Kolom x 2 Baris ==========
        Text(
            text = "Popular This Week",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter film berdasarkan genre
        val filteredMovies = if (selectedGenre == "All") {
            dummyPopular
        } else {
            dummyPopular.filter { it.genre == selectedGenre }
        }

        // Maksimal 8 film (2 baris x 4 kolom)
        val displayMovies = filteredMovies.take(8)

        // Grid 4 kolom
        val columns = 4
        for (i in displayMovies.indices step columns) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                for (j in 0 until columns) {
                    val index = i + j
                    if (index < displayMovies.size) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 3.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            displayMovies[index].posterColor,
                                            displayMovies[index].posterColor.copy(alpha = 0.5f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Text(
                                    text = displayMovies[index].title,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = displayMovies[index].genre,
                                    color = Color(0xFFB3B3B3),
                                    fontSize = 8.sp
                                )
                                Text(
                                    text = "⭐ ${displayMovies[index].rating}",
                                    color = Color(0xFFFFD700),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    } else {
                        // Placeholder kosong biar sejajar
                        Spacer(modifier = Modifier.weight(1f).padding(horizontal = 3.dp))
                    }
                }
            }
        }

        // Empty state
        if (displayMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No movies in this genre",
                    color = Color(0xFF757575),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}