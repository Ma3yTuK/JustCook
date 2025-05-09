package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class Ingredient(
    val id: Long,
    val name: String,
    val unit: MeasurementUnit
)