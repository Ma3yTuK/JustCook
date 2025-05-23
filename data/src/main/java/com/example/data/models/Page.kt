package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable data class PageData(
    val number: Long,
    val totalPages: Long,
    val size: Long,
    val totalElements: Long,
)

@Serializable
@Immutable data class Page<T>(
    val content: List<T>,
    val page: PageData,
)