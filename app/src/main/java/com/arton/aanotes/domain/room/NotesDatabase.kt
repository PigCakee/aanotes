package com.arton.aanotes.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arton.aanotes.domain.entity.NoteDto
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.room.converter.NotesConverter
import com.arton.aanotes.domain.room.dao.NotesDao
import com.arton.aanotes.domain.room.dao.TagsDao
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [NoteDto::class, Tag::class],
    version = MigrationHelper.LAST_VERSION,
    exportSchema = true
)
@TypeConverters(NotesConverter::class)
abstract class NotesDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(
            context: Context,
            databasePasswordManager: DatabasePasswordManager
        ): NotesDatabase {
            if (INSTANCE == null) {
                val password = SQLiteDatabase.getBytes(databasePasswordManager.getOrCreatePassword(context))
                val factory = SupportFactory(password)
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes.db"
                )
                    .openHelperFactory(factory)
                    .allowMainThreadQueries()
                    .addMigrations(*MigrationHelper.getMigrations())
                    .build() // TODO prepopulate db with tags
            }

            return requireNotNull(INSTANCE)
        }
    }

    abstract fun notesDao(): NotesDao
    abstract fun tagsDao(): TagsDao
}
