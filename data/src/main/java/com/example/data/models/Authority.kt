package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

enum class Authorities(val id: Long) {
    Moderate(3),
    Premium(4)
}

@Immutable
@Serializable
data class Authority(
    val id: Long,
    val name: String
)