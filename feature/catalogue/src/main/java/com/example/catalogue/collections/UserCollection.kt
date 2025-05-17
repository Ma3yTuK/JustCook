package com.example.catalogue.collections

import androidx.compose.runtime.Immutable
import com.example.catalogue.navigation.FilteredUserListRoute
import com.example.data.models.User
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserCollection(
    override val route: FilteredUserListRoute,
    override val entities: List<User> = RecipeService.usersFiltered(route)
) : EntityCollection