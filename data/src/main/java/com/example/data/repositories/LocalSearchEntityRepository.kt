package com.example.data.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.local.SearchEntry

@Dao
interface LocalSearchEntityRepository {
    @Query("SELECT * FROM search_entry ORDER BY moment DESC LIMIT :limit")
    fun getHistory(limit: Int): List<SearchEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntries(vararg searchEntries: SearchEntry)
}