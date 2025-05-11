package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class IngredientIngredientConversion(
    val id: Long,
    val measurementTo: MeasurementUnit,
    val coefficient: Double
)