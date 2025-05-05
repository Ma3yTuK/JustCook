package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeCollection(
    val id: Long,
    val name: String,
    val recipes: List<Recipe>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }