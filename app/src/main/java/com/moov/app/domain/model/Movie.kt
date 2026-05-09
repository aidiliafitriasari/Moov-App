package com.moov.app.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val tagline: String,
    val rating: Double,
    val duration: Int,
    val year: Int,
    val genres: List<String>,
    val synopsis: String,
    val posterUrl: String,
    val backdropUrl: String,
    val cast: List<String>
)