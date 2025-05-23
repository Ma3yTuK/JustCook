package com.example.data.services.review

import androidx.compose.runtime.compositionLocalOf
import androidx.paging.PagingSource
import com.example.data.models.Ingredient
import com.example.data.models.Page
import com.example.data.models.Recipe
import com.example.data.models.Review
import com.example.data.models.query_params.PageParams
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.MyPagingSource
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.UploadRecipeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

fun ReviewService.getPagingSource(recipeId: Long): PagingSource<Int, Review> {
    return MyPagingSource({ pageParams ->
        getAllReviews(recipeId, pageParams)
    })
}

interface ReviewService {

    @GET("/reviews/{recipeId}")
    suspend fun getAllReviews(@Path("recipeId") recipeId: Long, @QueryMap reviewRequestParams: Map<String, String>): Page<Review>

    @POST("/reviews/add")
    suspend fun addReview(@Body uploadReviewRequest: UploadReviewRequest): Review

    @POST("/reviews/delete/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: Long)
}

val LocalReviewService = compositionLocalOf<ReviewService?> { null }