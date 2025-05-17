package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class User(
    override val id: Long,
    val name: String,
    val email: String,
    val hasPremium: Boolean,
    val isVerified: Boolean,
    val authorities: List<Authority>
) : EntityWithId