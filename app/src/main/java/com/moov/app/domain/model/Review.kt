package com.moov.app.domain.model

data class Review(
    val id: Int,
    val userName: String,
    val rating: Int,
    val comment: String,
    val likeCount: Int,
    val timeAgo: String
)