package com.arton.aanotes.data.entity

import java.util.*

data class Note(
    var id: Long = 0,

    var title: String = "",

    var body: String = "",

    var createdAt: Date,

    var editedAt: Date,

    var tags: List<Tag> = listOf()
) {
    fun mapToDto() = NoteDto(id, title, body, createdAt, editedAt, tags)
}