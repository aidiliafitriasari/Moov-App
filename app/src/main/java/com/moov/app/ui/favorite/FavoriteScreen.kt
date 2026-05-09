package com.moov.app.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

data class FavoriteMovie(
    val id: Int,
    val title: String,
    val subtitle: String,
    val genre: String,
    val rating: Double,
    val posterColor: Color
)

@Composable
fun FavoriteScreen() {
    val dummyFavorites = remember {
        mutableStateListOf(
            FavoriteMovie(1, "Komang", "LEBARAN 2025", "Drama Romantis", 8.0, Color(0xFFFFA500)),
            FavoriteMovie(2, "Jumbo", "LEBARAN 2025 DI BIOSKOP", "Petualangan", 8.9, Color(0xFF4169E1)),
            FavoriteMovie(3, "Sekawan Limo", "4 JULI 2024", "Sekawan Limo", 7.8, Color(0xFF8A2BE2)),
            FavoriteMovie(4, "Janur Irong Sewu Dino", "24 DECEMBER 2025", "Janur Irong Sewu Dino", 8.7, Color(0xFF2F4F4F))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Favorite",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Empty state
        if (dummyFavorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your watchlist is empty",
                    color = Color(0xFF757575),
                    fontSize = 14.sp
                )
            }
        }

        // Grid 2 kolom
        val chunkedMovies = dummyFavorites.chunked(2)
        chunkedMovies.forEach { rowMovies ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowMovies.forEach { movie ->
                    FavoriteCard(
                        movie = movie,
                        onDelete = { dummyFavorites.remove(movie) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Jika baris terakhir hanya 1 item, isi sisa dengan spacer
                if (rowMovies.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun FavoriteCard(movie: FavoriteMovie, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column {
            // Poster (bagian atas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                movie.posterColor,
                                movie.posterColor.copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🎬", fontSize = 48.sp)

                // Tombol hapus di pojok kanan bawah poster
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus dari favorit",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Info film (bagian bawah)
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Judul
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movie.rating}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Genre
                Text(
                    text = movie.genre,
                    color = Color(0xFF757575),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}