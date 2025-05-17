package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserSortingOption(
    override val id: Long,
    override val name: String
) : SortEntity