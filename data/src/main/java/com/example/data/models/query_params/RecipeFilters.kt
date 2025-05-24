package com.example.data.models.query_params

import com.example.data.models.Ingredient
import com.example.data.models.short_models.UserShort

interface RecipeFilters : Filters {
    val sortingOption: String?
    val sortingAscending: Boolean
    val users: Set<UserShort>
    val categoryIds: List<Long>
    val lifeStyleIds: List<Long>
    val ingredients: Set<Ingredient>

    override fun toMap(): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()
        sortingOption?.let { result["sortingOption"] = it }
        result["sortingAscending"] = sortingAscending.toString()
        result["userIds"] = users.joinToString(",") { it.id.toString() }
        result["categoryIds"] = categoryIds.joinToString(",")
        result["lifeStyleIds"] = lifeStyleIds.joinToString(",")
        result["ingredientIds"] = ingredients.joinToString(",") { it.id.toString() }
        return result
    }
}

open class RecipeFiltersDefault(
    override val sortingOption: String? = null,
    override val sortingAscending: Boolean = false,
    override val users: Set<UserShort> = setOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredients: Set<Ingredient> = setOf()
) : RecipeFilters {
    constructor() : this(null)
}