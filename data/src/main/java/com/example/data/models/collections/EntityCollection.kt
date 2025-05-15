package com.example.data.models.collections

abstract class EntityCollection {
    abstract val id: Long
    abstract val name: String
    abstract val entities: List<Any>
}