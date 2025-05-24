package com.example.data.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "search_entry")
data class SearchEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val entry: String,
    val moment: Long = Date().time
)