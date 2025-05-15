package com.example.data.models.collections

import androidx.compose.runtime.Immutable
import com.example.data.models.Recipe
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeCollection(
    override val id: Long,
    override val name: String,
    override val entities: List<Recipe>,
) : EntityCollection()