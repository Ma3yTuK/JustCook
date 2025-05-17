package com.example.data.models.categories

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class CategoryCollection(
    val name: String,
    val categories: List<Category>
)