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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moov.app.data.local.FavoriteMovieEntity
import com.moov.app.data.local.MovieDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@Composable
fun FavoriteScreen() {
    val context = LocalContext.current
    val db = remember { MovieDatabase.getDatabase(context) }
    val dao = remember { db.favoriteMovieDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var favorites by remember { mutableStateOf<List<FavoriteMovieEntity>>(emptyList()) }

    LaunchedEffect(userId) {
        favorites = dao.getFavoritesByUser(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Favorite",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your watchlist is empty",
                    color = Color(0xFF757575),
                    fontSize = 14.sp
                )
            }
        }

        val chunkedMovies = favorites.chunked(2)
        chunkedMovies.forEach { rowMovies ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowMovies.forEach { movie ->
                    FavoriteCard(
                        movie = movie,
                        onDelete = {
                            // Hapus dari Room DB
                            kotlinx.coroutines.MainScope().launch {
                                dao.deleteFavorite(movie)
                                favorites = dao.getFavoritesByUser(userId)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
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
fun FavoriteCard(movie: FavoriteMovieEntity, onDelete: () -> Unit, modifier: Modifier = Modifier) {
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
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center

            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize()
                )

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
                        text = "${movie.voteAverage}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Genre
                Text(
                    text = movie.genreIds,
                    color = Color(0xFF757575),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}