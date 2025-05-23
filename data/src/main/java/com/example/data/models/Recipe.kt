package com.example.data.models

import androidx.compose.runtime.Immutable
import com.example.data.models.short_models.RecipeEntity
import com.example.data.models.short_models.UserShort
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class Recipe(
    override val id: Long,
    override val name: String,
    override val calories: Long,
    override val rating: Float,
    val description: String,
    override val weight: Long,
    val isVerified: Boolean,
    override val isFavorite: Boolean,
    override val isPremium: Boolean,
    val user: UserShort,
    val steps: List<RecipeStep>,
    val ingredients: List<RecipeConversion>,
    override val image: Image? = null
) : RecipeEntity