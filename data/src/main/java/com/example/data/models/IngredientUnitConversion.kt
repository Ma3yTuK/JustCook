package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class IngredientUnitConversion(
    override val id: Long,
    val ingredient: Ingredient,
    val unit: MeasurementUnit,
    val coefficient: Double
) : EntityWithId