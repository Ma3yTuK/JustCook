package com.example.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable data class MeasurementUnit(
    override val id: Long,
    val name: String
) : EntityWithId