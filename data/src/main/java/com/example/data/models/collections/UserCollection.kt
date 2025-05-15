package com.example.data.models.collections

import androidx.compose.runtime.Immutable
import com.example.data.models.User
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserCollection(
    override val id: Long,
    override val name: String,
    override val entities: List<User>
) : EntityCollection()