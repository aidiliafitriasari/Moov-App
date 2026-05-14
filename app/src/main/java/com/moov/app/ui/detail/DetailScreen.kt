package com.moov.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moov.app.domain.model.Review
import com.moov.app.ui.components.RatingDialog
import com.moov.app.data.remote.TmdbMovieDto
import coil.compose.AsyncImage
import com.moov.app.data.local.FavoriteMovieEntity
import com.moov.app.data.local.MovieDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(movie: TmdbMovieDto, onBack: () -> Unit) {
    val dummyReviews = listOf(
        Review(
            id = 1, userName = "Marselino", rating = 5,
            comment = "Sumpah gila bagus bangett bener bener alurnya ga bisa ketebak, mana ditambah ada horror nya, actionnya top",
            likeCount = 5, timeAgo = "10 menit yang lalu"
        ),
        Review(
            id = 2, userName = "Rehania", rating = 5,
            comment = "ANGGA SHENNA BENER BENER TOP MARKOTOP ACTINGNYA GA PERNAH GAGAL",
            likeCount = 10, timeAgo = "1 jam yang lalu"
        )
    )

    var isFavorite by remember { mutableStateOf(false) }
    var showRatingDialog by remember { mutableStateOf(false) }
    var reviews by remember { mutableStateOf(dummyReviews) }
    var userGivenRating by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val db = remember { MovieDatabase.getDatabase(context) }
    val dao = remember { db.favoriteMovieDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Cek apakah film ini udah ada di favorit
    LaunchedEffect(movie.id) {
        isFavorite = dao.isFavorite(movie.id, userId) != null
    }

    if (showRatingDialog) {
        RatingDialog(
            onDismiss = { showRatingDialog = false },
            onSubmit = { rating, comment ->
                reviews = reviews + Review(
                    id = reviews.size + 1,
                    userName = "You",
                    rating = rating,
                    comment = comment,
                    likeCount = 0,
                    timeAgo = "Baru saja"
                )
                userGivenRating = rating
                showRatingDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Backdrop
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.DarkGray)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.backdrop_path}",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 40.dp)
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Title + Favorite
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = movie.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = movie.overview.take(50) + "...",
                        fontSize = 14.sp,
                        color = Color(0xFFB3B3B3)
                    )
                }
                IconButton(onClick = {
                    kotlinx.coroutines.MainScope().launch {
                        if (isFavorite) {
                            // Hapus dari favorit
                            dao.isFavorite(movie.id, userId)?.let { dao.deleteFavorite(it) }
                            isFavorite = false
                        } else {
                            // Tambah ke favorit
                            dao.insertFavorite(
                                FavoriteMovieEntity(
                                    id = movie.id,
                                    title = movie.title,
                                    overview = movie.overview,
                                    posterPath = movie.poster_path,
                                    backdropPath = movie.backdrop_path,
                                    voteAverage = movie.vote_average,
                                    releaseDate = movie.release_date,
                                    genreIds = movie.genre_ids?.joinToString(",") ?: "",
                                    userId = userId
                                )
                            )
                            isFavorite = true
                        }
                    }
                }) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        "Favorite",
                        tint = if (isFavorite) Color(0xFFE50914) else Color(0xFF757575),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating, Duration, Year
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        tint = Color(0xFFF5C518),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${movie.vote_average}/10", color = Color.White, fontSize = 16.sp)
                }
                Text(
                    movie.release_date?.take(4) ?: "N/A",
                    color = Color(0xFFB3B3B3),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Genres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movie.genre_ids?.take(3)?.forEach { genreId ->
                    Text(
                        text = genreId.toString(),
                        color = Color(0xFFB3B3B3),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(Color(0xFF242424), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        kotlinx.coroutines.MainScope().launch {
                            if (isFavorite) {
                                dao.isFavorite(movie.id, userId)?.let { dao.deleteFavorite(it) }
                                isFavorite = false
                            } else {
                                dao.insertFavorite(
                                    FavoriteMovieEntity(
                                        id = movie.id,
                                        title = movie.title,
                                        overview = movie.overview,
                                        posterPath = movie.poster_path,
                                        backdropPath = movie.backdrop_path,
                                        voteAverage = movie.vote_average,
                                        releaseDate = movie.release_date,
                                        genreIds = movie.genre_ids?.joinToString(",") ?: "",
                                        userId = userId
                                    )
                                )
                                isFavorite = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isFavorite) "Favorite" else "Add to Favorite",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color.White)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Watch Trailer",
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Synopsis
            Text(
                "Synopsis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.overview,
                fontSize = 14.sp,
                color = Color(0xFFB3B3B3),
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Your Rating
            Text(
                "Your Rating",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { showRatingDialog = true }
            ) {
                for (i in 1..5) {
                    Icon(
                        Icons.Filled.Star, null,
                        tint = if (i <= userGivenRating) Color(0xFFF5C518) else Color(0xFF2E2E2E),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (userGivenRating == 0) "Tap to rate" else "Your rating: $userGivenRating/5",
                    color = Color(0xFF757575),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reviews
            Text(
                "Ulasan Pengguna (${reviews.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            reviews.forEach { review ->
                ReviewCard(review = review)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE50914)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            review.userName.first().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            review.userName,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(review.timeAgo, color = Color(0xFF757575), fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            Icons.Filled.Star, null,
                            tint = if (i <= review.rating) Color(0xFFF5C518) else Color(0xFF2E2E2E),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    review.comment,
                    color = Color(0xFFB3B3B3),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "👏 ${review.likeCount}",
                    color = Color(0xFF757575),
                    fontSize = 12.sp
                )
            }
        }
    }
