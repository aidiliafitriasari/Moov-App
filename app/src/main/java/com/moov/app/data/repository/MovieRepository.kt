package com.moov.app.data.repository

import com.moov.app.data.remote.RetrofitClient
import com.moov.app.data.remote.TmdbMovieResponse

class MovieRepository {
    private val apiKey = "3d6e611f09149f61e5bc1727dc052da0"
    private val apiService = RetrofitClient.apiService

    suspend fun getTrendingMovies(): TmdbMovieResponse {
        return apiService.getTrendingMovies(apiKey)
    }

    suspend fun getNowPlayingMovies(): TmdbMovieResponse {
        return apiService.getNowPlayingMovies(apiKey)
    }

    suspend fun getPopularMovies(): TmdbMovieResponse {
        return apiService.getPopularMovies(apiKey)
    }

    // Tambahan untuk Trailer
    suspend fun getMovieTrailerKey(movieId: Int): String? {
        return try {
            val response = RetrofitClient.apiService.getMovieVideos(
                movieId = movieId,
                apiKey = apiKey
            )
            // Cari trailer resmi dari YouTube, jika tidak ada ambil video YouTube pertama
            response.results.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }?.key
                ?: response.results.firstOrNull { it.site == "YouTube" }?.key
        } catch (e: Exception) {
            null
        }
    }

    suspend fun searchMovies(query: String): TmdbMovieResponse {
        return RetrofitClient.apiService.searchMovies(apiKey, query)
    }
}