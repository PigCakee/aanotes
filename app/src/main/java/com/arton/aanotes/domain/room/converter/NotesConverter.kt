package com.arton.aanotes.domain.room.converter

import androidx.room.TypeConverter
import com.arton.aanotes.domain.entity.Tag
import com.google.gson.Gson
import java.util.*

class NotesConverter {

    @TypeConverter
    fun fromTagList(list: List<Tag>): String = Gson().toJson(list)

    @TypeConverter
    fun toTagList(string: String): List<Tag> = Gson().fromJson(string, listOf<Tag>()::class.java)

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}