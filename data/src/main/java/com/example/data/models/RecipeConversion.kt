package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeConversion(
    override val id: Long,
    val amount: Long,
    val ingredientUnitConversion: IngredientUnitConversion
) : EntityWithId