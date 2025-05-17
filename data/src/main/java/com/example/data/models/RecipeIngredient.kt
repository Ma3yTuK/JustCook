package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeIngredient(
    override val id: Long,
    val amount: Float,
    val ingredient: Ingredient,
    val ingredientConversion: IngredientIngredientConversion? = null
) : EntityWithId