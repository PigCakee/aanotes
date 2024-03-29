package com.arton.aanotes.domain.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arton.aanotes.data.entity.NoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%' ORDER BY editedAt DESC")
    fun getNotes(query: String = ""): Flow<List<NoteDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteDto: NoteDto)

    @Delete
    suspend fun deleteNote(noteDto: NoteDto)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}