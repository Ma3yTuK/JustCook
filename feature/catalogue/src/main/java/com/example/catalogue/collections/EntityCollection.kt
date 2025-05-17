package com.example.catalogue.collections

import com.example.data.models.EntityWithId

interface CollectionDescription {
    val title: String
}

interface EntityCollection {
    val entities: List<EntityWithId>
    val route: CollectionDescription
}