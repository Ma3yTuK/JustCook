package com.example.data.models.categories

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class RealCategory(
    override val id: Long,
    override val name: String
) : Category