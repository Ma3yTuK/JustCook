package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class Recipe(
    val id: Long,
    val name: String,
    val calories: Long,
    val rating: Float,
    val description: String,
    val isFavorite: Boolean,
    val isPremium: Boolean,
    val user: User,
    val steps: List<RecipeStep>,
    val ingredients: List<RecipeIngredient>,
    val reviews: List<Review>
)