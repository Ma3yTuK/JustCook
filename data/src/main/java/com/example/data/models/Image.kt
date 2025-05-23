package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Image(
    override val id: Long,
    val location: String
) : EntityWithId