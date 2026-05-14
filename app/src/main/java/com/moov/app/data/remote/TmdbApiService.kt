package com.moov.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): TmdbVideoResponse
}