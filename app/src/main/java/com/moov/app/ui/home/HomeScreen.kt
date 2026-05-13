package com.moov.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.moov.app.data.remote.TmdbMovieDto
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToDetail: (TmdbMovieDto) -> Unit = {}) {
    val viewModel: HomeViewModel = viewModel()
    val trendingMovies = viewModel.trendingMovies.value
    val nowPlayingMovies = viewModel.nowPlayingMovies.value
    val popularMovies = viewModel.popularMovies.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userName = currentUser?.displayName ?: "User"
    val userInitial = if (userName != "User") userName.first().uppercaseChar().toString() else "U"

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (isLoading) {
            Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE50914))
            }
            return@Column
        }

        if (errorMessage != null) {
            Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                Text("Error: $errorMessage", color = Color.White)
            }
            return@Column
        }

        // ========== HEADER: Foto Profil + Welcome ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE50914)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInitial,
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
                    text = userName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ========== TRENDING NOW ==========
        Text(
            text = "Trending Now",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        var trendingIndex by remember { mutableIntStateOf(0) }
        val scrollState = rememberScrollState()

        LaunchedEffect(scrollState.value) {
            val itemWidth = 340
            trendingIndex = (scrollState.value / itemWidth).coerceIn(0, trendingMovies.size - 1)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.horizontalScroll(scrollState)
            ) {
                trendingMovies.forEach { movie ->
                    Box(
                        modifier = Modifier
                            .width(328.dp)
                            .height(180.dp)
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.DarkGray)
                            .clickable { onNavigateToDetail(movie) },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                            contentDescription = movie.title,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.8f)
                                        )
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
                                    text = "⭐ ${movie.vote_average}",
                                    color = Color(0xFFFFD700),
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = movie.genre_ids.firstOrNull()?.toString() ?: "",
                                    color = Color(0xFFB3B3B3),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                trendingMovies.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == trendingIndex) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == trendingIndex) Color(0xFFE50914) else Color(
                                    0xFF757575
                                )
                            )
                    )
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
                                color = if (selectedGenre == genre) Color.White else Color(
                                    0xFFB3B3B3
                                )
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

            // ========== POPULAR THIS WEEK ==========
            Text(
                text = "Popular This Week",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val displayMovies = popularMovies.take(8)

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
                                    .background(Color.DarkGray)
                                    .clickable { onNavigateToDetail(displayMovies[index]) },
                                contentAlignment = Alignment.BottomStart
                            ) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w500${displayMovies[index].poster_path}",
                                    contentDescription = displayMovies[index].title,
                                    modifier = Modifier.fillMaxSize()
                                )
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
                                        text = displayMovies[index].genre_ids.firstOrNull()
                                            ?.toString() ?: "",
                                        color = Color(0xFFB3B3B3),
                                        fontSize = 8.sp
                                    )
                                    Text(
                                        text = "⭐ ${displayMovies[index].vote_average}",
                                        color = Color(0xFFFFD700),
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).padding(horizontal = 3.dp))
                        }
                    }
                }
            }

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
}