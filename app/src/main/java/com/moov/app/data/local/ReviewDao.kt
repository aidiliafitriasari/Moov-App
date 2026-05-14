package com.moov.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDao {

    @Insert
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE movieId = :movieId")
    suspend fun getReviewsByMovie(movieId: Int): List<ReviewEntity>

    @Query("UPDATE reviews SET likeCount = likeCount + 1 WHERE id = :reviewId")
    suspend fun likeReview(reviewId: Int)
}