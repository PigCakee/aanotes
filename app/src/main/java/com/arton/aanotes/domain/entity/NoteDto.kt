package com.arton.aanotes.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDto(
    @PrimaryKey(autoGenerate = false)
    var id: String = ""
)
