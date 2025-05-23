package com.example.data.models.query_params

interface RecipeFilters : Filters {
    val sortingOption: String?
    val userIds: List<Long>
    val categoryIds: List<Long>
    val lifeStyleIds: List<Long>
    val ingredientIds: List<Long>

    override fun toMap(): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf();
        sortingOption?.let { result["sortingOption"] = it }
        result["userIds"] = userIds.joinToString(",")
        result["categoryIds"] = categoryIds.joinToString(",")
        result["lifeStyleIds"] = lifeStyleIds.joinToString(",")
        result["ingredientIds"] = ingredientIds.joinToString(",")
        return result
    }
}

open class RecipeFiltersDefault(
    override val sortingOption: String? = null,
    override val userIds: List<Long> = listOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredientIds: List<Long> = listOf(),
) : RecipeFilters {
    constructor() : this(null)
}