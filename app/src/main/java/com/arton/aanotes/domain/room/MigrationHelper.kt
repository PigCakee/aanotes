package com.arton.aanotes.domain.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationHelper {

    companion object {

        const val LAST_VERSION = 1

        fun getMigrations(): Array<Migration> {
            return arrayOf()
        }
    }
}
