package com.example.data.models.categories

import com.example.data.models.EntityWithId
import com.example.data.models.Image

interface Category : EntityWithId {
    override val id: Long
    val name: String
    val image: Image?
}