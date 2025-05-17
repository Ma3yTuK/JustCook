package com.example.data.models.search_suggestions

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SearchSuggestion(
    val suggestion: String
)