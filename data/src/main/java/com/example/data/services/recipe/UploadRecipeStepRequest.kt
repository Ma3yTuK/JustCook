package com.example.data.services.recipe

import com.example.data.models.RecipeStep

data class UploadRecipeStepRequest(
    val information: String? = null,
    val imageId: Long? = null,
    val ind: Long? = null
) {
    constructor(recipeStep: RecipeStep) : this(
        information = recipeStep.information,
        imageId = recipeStep.image?.id,
        ind = recipeStep.ind
    )
}