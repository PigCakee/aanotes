package com.arton.aanotes.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class NoteDto(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String,

    var body: String,

    var createdAt: Date,

    var editedAt: Date,

    var tags: List<Tag> = listOf()
) {
    fun mapToEntity() = Note(id, title, body, createdAt, editedAt, tags)
}
