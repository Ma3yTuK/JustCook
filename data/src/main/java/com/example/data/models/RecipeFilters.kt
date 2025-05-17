package com.example.data.models

interface RecipeFilters {
    val sortingOptionId: Long?
    val userIds: List<Long>
    val categoryIds: List<Long>
    val lifeStyleIds: List<Long>
    val ingredientIds: List<Long>
}

open class RecipeFiltersDefault(
    override val sortingOptionId: Long? = null,
    override val userIds: List<Long> = listOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf(),
    override val ingredientIds: List<Long> = listOf(),
) : RecipeFilters {
    constructor() : this(null)
}