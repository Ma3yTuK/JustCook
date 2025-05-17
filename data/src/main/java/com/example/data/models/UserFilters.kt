package com.example.data.models

interface UserFilters {
    val sortingOptionId: Long?
}

open class UserFiltersDefault(
    override val sortingOptionId: Long? = null
) : UserFilters {
    constructor() : this(null)
}