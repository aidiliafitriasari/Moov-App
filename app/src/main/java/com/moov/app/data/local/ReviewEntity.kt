package com.moov.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieId: Int,
    val userId: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val likeCount: Int = 0,
    val timeAgo: String
)