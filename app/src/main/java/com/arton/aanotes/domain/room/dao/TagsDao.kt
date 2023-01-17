package com.arton.aanotes.domain.room.dao

import androidx.room.*
import com.arton.aanotes.data.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {

    @Query("SELECT * FROM tags")
    fun getTags(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("DELETE FROM tags")
    suspend fun deleteAll()
}