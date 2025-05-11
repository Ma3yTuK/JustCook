package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Review(
    val id: Long,
    val rating: Long,
    val comment: String,
    val date: LocalDateTime,
    val user: User
)