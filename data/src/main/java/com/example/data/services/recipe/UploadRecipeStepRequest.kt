package com.example.data.services.recipe

import com.example.data.models.RecipeStep

data class UploadRecipeStepRequest(
    val description: String? = null,
    val imageId: Long? = null,
    val index: Long? = null
) {
    constructor(recipeStep: RecipeStep) : this(
        description = recipeStep.description,
        imageId = recipeStep.image?.id,
        index = recipeStep.index
    )
}