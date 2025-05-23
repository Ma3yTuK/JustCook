package com.example.data.models.short_models

import androidx.compose.runtime.Immutable
import com.example.data.models.Authority
import com.example.data.models.EntityWithId
import com.example.data.models.Image
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserShort(
    override val id: Long,
    val name: String,
    val email: String,
    val isVerified: Boolean,
    val image: Image? = null
) : EntityWithId