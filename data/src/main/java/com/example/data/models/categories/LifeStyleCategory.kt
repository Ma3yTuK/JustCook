package com.example.data.models.categories

import androidx.compose.runtime.Immutable
import com.example.data.models.Image
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class LifeStyleCategory(
    override val id: Long,
    override val name: String,
    override val image: Image? = null
) : Category