package com.example.data.services.review

import com.example.data.models.Review

data class UploadReviewRequest(
    val rating: Long? = null,
    val recipeId: Long? = null,
    val comment: String? = null
) {
    constructor(review: Review, recipeId: Long?) : this(
        rating = review.rating,
        recipeId = recipeId,
        comment = review.comment
    )
}