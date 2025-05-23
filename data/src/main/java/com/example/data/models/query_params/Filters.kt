package com.example.data.models.query_params

interface Filters {
    fun toMap(): Map<String, String>
}