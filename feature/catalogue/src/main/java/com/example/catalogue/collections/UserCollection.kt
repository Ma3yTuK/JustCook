package com.example.catalogue.collections

import androidx.compose.runtime.Immutable
import com.example.catalogue.user_list_page.FilteredUserListRoute
import com.example.data.models.short_models.UserShort
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserCollection(
    override val route: FilteredUserListRoute,
    override val entities: List<UserShort>
) : EntityCollection