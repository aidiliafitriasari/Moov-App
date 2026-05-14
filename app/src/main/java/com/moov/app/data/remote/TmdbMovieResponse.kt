package com.moov.app.data.remote

data class TmdbMovieResponse(
    val results: List<TmdbMovieDto>
)

data class TmdbMovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val genre_ids: List<Int>,
    val release_date: String?
)

data class TmdbVideoResponse(
    val results: List<TmdbVideoDto>
)

data class TmdbVideoDto(
    val key: String?,
    val name: String?,
    val site: String?,
    val type: String?
)