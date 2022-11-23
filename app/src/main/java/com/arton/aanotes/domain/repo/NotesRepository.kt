package com.arton.aanotes.domain.repo

import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.room.dao.NotesDao
import com.arton.aanotes.domain.room.dao.TagsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val notesDao: NotesDao,
    private val tagsDao: TagsDao
) {

    val notes: Flow<List<Note>> = notesDao.getNotes().map { noteDtos ->
        noteDtos.map { it.mapToEntity() }
    }

    val tags: Flow<List<Tag>> = tagsDao.getTags()

    fun getNotes(query: String) {
        notesDao.getNotes(query)
    }

    fun deleteNote(note: Note) {
        notesDao.deleteNote(note.mapToDto())
    }

    fun insertNote(note: Note) {
        notesDao.insertNote(note.mapToDto())
    }
}