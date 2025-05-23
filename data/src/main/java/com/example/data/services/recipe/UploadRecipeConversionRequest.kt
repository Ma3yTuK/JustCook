package com.example.data.services.recipe

import com.example.data.models.RecipeConversion

data class UploadRecipeConversionRequest(
    val amount: Long? = null,
    val conversionId: Long? = null
) {
    constructor(recipeConversion: RecipeConversion) : this(
        amount = recipeConversion.amount,
        conversionId = recipeConversion.id
    )
}