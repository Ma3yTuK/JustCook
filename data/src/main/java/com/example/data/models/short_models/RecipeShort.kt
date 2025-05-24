package com.example.data.models.short_models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import com.example.data.models.EntityWithId
import com.example.data.models.Image
import kotlinx.serialization.Serializable

interface RecipeEntity : EntityWithId {
    override val id: Long
    val name: String
    val calories: Long
    val rating: Float
    val isVerified: Boolean
    val isFavorite: Boolean
    val weight: Long
    val image: Image?
    val isPremium: Boolean
}

@Immutable
@Serializable
data class RecipeShort(
    override val id: Long,
    override val weight: Long,
    override val name: String,
    override val isVerified: Boolean,
    override val calories: Long,
    override val rating: Float,
    override val isFavorite: Boolean,
    override val image: Image?,
    override val isPremium: Boolean
) : RecipeEntity