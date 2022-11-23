package com.arton.aanotes.domain.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arton.aanotes.domain.entity.NoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes WHERE title LIKE :query OR body LIKE :query")
    fun getNotes(query: String = ""): Flow<List<NoteDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteDto: NoteDto)

    @Delete
    fun deleteNote(noteDto: NoteDto)
}