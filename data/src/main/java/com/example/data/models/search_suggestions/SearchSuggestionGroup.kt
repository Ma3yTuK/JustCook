package com.example.data.models.search_suggestions

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SearchSuggestionGroup(
    val name: String,
    val suggestions: List<SearchSuggestion>
)