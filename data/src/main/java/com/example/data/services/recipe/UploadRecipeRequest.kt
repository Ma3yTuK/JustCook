package com.example.data.services.recipe

import com.example.data.models.Recipe
import com.example.data.models.RecipeStep

data class UploadRecipeRequest(
    val name: String? = null,
    val weight: Long? = null,
    val information: String? = null,
    val steps: List<Long>? = null,
    val ingredients: List<Long>? = null,
    val imageId: Long? = null
) {
    constructor(recipe: Recipe) : this(
        name = recipe.name,
        weight = recipe.weight,
        information = recipe.information,
        steps = recipe.steps.map { it.id },
        ingredients = recipe.ingredients.map { it.id },
        imageId = recipe.image?.id
    )
}