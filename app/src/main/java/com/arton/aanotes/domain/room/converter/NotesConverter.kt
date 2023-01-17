package com.arton.aanotes.domain.room.converter

import androidx.room.TypeConverter
import com.arton.aanotes.data.entity.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NotesConverter {

    @TypeConverter
    fun fromTagList(list: List<Tag>): String = Gson().toJson(list)

    @TypeConverter
    fun toTagList(string: String): List<Tag> = Gson().fromJson(string, object : TypeToken<List<Tag>>(){}.type)

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}