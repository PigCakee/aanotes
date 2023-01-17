package com.arton.aanotes.domain.room

import androidx.room.migration.Migration

class MigrationHelper {

    companion object {

        const val LAST_VERSION = 1

        fun getMigrations(): Array<Migration> {
            return arrayOf()
        }
    }
}
