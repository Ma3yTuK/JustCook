package com.example.data.models

import androidx.compose.runtime.Immutable
import com.example.data.models.short_models.UserShort
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Review(
    override val id: Long,
    val rating: Long,
    val comment: String,
    val moment: Long,
    val user: UserShort
) : EntityWithId