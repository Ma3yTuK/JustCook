package com.example.data.models.categories

import com.example.data.models.EntityWithId

interface Category : EntityWithId {
    override val id: Long
    val name: String
}