package com.example.catalogue.collections

import androidx.compose.runtime.Immutable
import com.example.catalogue.navigation.FilteredRecipeListRoute
import com.example.catalogue.navigation.FilteredUserListRoute
import com.example.data.models.Recipe
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class RecipeCollection(
    override val route: FilteredRecipeListRoute,
    override val entities: List<Recipe> = RecipeService.recipesFiltered(route)
) : EntityCollection