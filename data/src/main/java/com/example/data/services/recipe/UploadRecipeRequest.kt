package com.example.data.services.recipe

import com.example.data.models.Recipe
import com.example.data.models.RecipeStep

data class UploadRecipeRequest(
    val name: String? = null,
    val weight: Long? = null,
    val description: String? = null,
    val steps: List<Long>? = null,
    val ingredients: List<Long>? = null,
    val imageId: Long? = null
) {
    constructor(recipe: Recipe) : this(
        name = recipe.name,
        weight = recipe.weight,
        description = recipe.description,
        steps = recipe.steps.map { it.id },
        ingredients = recipe.ingredients.map { it.id },
        imageId = recipe.image?.id
    )
}