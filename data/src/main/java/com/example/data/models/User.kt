package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val authorities: List<Authority>
)