package com.arton.aanotes.domain.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.arton.aanotes.domain.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {

    @Query("SELECT * FROM tags")
    fun getTags(): Flow<List<Tag>>
}