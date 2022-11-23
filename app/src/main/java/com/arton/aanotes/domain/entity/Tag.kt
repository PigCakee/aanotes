package com.arton.aanotes.domain.entity

import androidx.room.Entity

@Entity(tableName = "tags")
data class Tag(
    val name: String
)