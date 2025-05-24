package com.example.search.search_state

import com.example.data.models.Ingredient
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.UserFilters
import com.example.data.models.short_models.UserShort
import com.example.search.R

enum class SearchType(val displayedNameId: Int) {
    User(R.string.user_display),
    Recipe(R.string.recipe_display)
}

data class SearchState(
    val searchType: SearchType = SearchType.Recipe,
    override val isVerified: Boolean? = null,
    override val sortingOption: String? = null,
    override val sortingAscending: Boolean = false,
    override val users: Set<UserShort> = setOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredients: Set<Ingredient> = setOf(),
) : RecipeFilters, UserFilters {
    override fun toMap(): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()
        sortingOption?.let { result["sortingOption"] = it }
        isVerified?.let { result["isVerified"] = it.toString() }
        result["sortingAscending"] = sortingAscending.toString()
        result["userIds"] = users.joinToString(",") { it.id.toString() }
        result["categoryIds"] = categoryIds.joinToString(",")
        result["lifeStyleIds"] = lifeStyleIds.joinToString(",")
        result["ingredientIds"] = ingredients.joinToString(",") { it.id.toString() }
        return result
    }
}