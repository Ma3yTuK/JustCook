package com.example.search.search_state

import android.content.res.Resources
import com.example.data.models.RecipeFilters
import com.example.data.models.UserFilters
import com.example.search.R

enum class SearchType(val displayedNameId: Int) {
    User(R.string.user_display),
    Recipe(R.string.recipe_display)
}

data class SearchState(
    val searchType: SearchType = SearchType.Recipe,
    override val sortingOptionId: Long? = null,
    override val userIds: List<Long> = listOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredientIds: List<Long> = listOf()
) : RecipeFilters, UserFilters