package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeStep(
    override val id: Long,
    val description: String
) : EntityWithId