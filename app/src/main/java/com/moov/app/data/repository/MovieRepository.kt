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
}