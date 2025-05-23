package com.example.data.models.query_params

data class PageParams(
    val offset: Long,
    val limit: Long
) {
    fun toMap() = mapOf(
        "offset" to offset.toString(),
        "limit" to limit.toString()
    )
}