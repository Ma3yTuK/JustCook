package com.example.data.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.models.local.SearchEntry
import com.example.data.repositories.LocalSearchEntityRepository

@Database(entities = [SearchEntry::class], version = 1)
abstract class SuggestionDatabase : RoomDatabase() {
    abstract fun localSearchEntityRepository(): LocalSearchEntityRepository
}