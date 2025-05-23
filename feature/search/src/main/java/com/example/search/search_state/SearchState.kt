package com.example.search.search_state

import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.UserFilters
import com.example.search.R

enum class SearchType(val displayedNameId: Int) {
    User(R.string.user_display),
    Recipe(R.string.recipe_display)
}

data class SearchState(
    val searchType: SearchType = SearchType.Recipe,
    override val isVerified: Boolean? = null,
    override val sortingOption: String? = null,
    override val userIds: List<Long> = listOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredientIds: List<Long> = listOf(),
) : RecipeFilters, UserFilters {
    override fun toMap(): Map<String, String> {
        return mapOf<String, String>()
    }
}