package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class Ingredient(
    override val id: Long,
    val calories: Long,
    val name: String
) : EntityWithId