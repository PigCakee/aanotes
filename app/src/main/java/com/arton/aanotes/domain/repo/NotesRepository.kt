package com.arton.aanotes.domain.repo

import com.arton.aanotes.data.DataStoreManager
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.room.dao.NotesDao
import com.arton.aanotes.domain.room.dao.TagsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val notesDao: NotesDao,
    private val tagsDao: TagsDao,
    private val dataStoreManager: DataStoreManager
) {

    val notes = notesDao.getNotes().map { noteDtos ->
        noteDtos.map { it.mapToEntity() }
    }

    val tags: Flow<List<Tag>> = tagsDao.getTags()

    val currentNoteId = dataStoreManager.currentNoteId

    val isSharingEnabled = dataStoreManager.isSharingEnabled

    fun getNotes(query: String = "") = notesDao.getNotes(query)

    suspend fun insertTag(tag: Tag) {
        tagsDao.insertTag(tag)
    }

    suspend fun deleteTag(tag: Tag) {
        tagsDao.deleteTag(tag)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note.mapToDto())
    }

    suspend fun insertNote(note: Note) {
        notesDao.insertNote(note.mapToDto())
    }

    suspend fun setCurrentNote(note: Note) {
        insertNote(note)
        dataStoreManager.setCurrentNoteId(note.id)
    }
}