package com.example.catalogue.collections

import androidx.compose.runtime.Immutable
import com.example.catalogue.recipe_list_page.FilteredRecipeListRoute
import com.example.data.models.short_models.RecipeShort
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeCollection(
    override val route: FilteredRecipeListRoute,
    override val entities: List<RecipeShort>
) : EntityCollection