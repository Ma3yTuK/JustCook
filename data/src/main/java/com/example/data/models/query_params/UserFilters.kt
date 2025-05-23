package com.example.data.models.query_params

interface UserFilters : Filters {
    val sortingOption: String?
    val isVerified: Boolean?

    override fun toMap(): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()
        sortingOption?.let { result["sortingOption"] = it }
        isVerified?.let { result["isVerified"] = it.toString() }
        return result
    }
}

open class UserFiltersDefault(
    override val isVerified: Boolean? = null,
    override val sortingOption: String? = null
) : UserFilters {
    constructor() : this(null)
}