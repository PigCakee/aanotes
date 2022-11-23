package com.arton.aanotes.presentation.di

import android.content.Context
import com.arton.aanotes.common.utils.CryptoManager
import com.arton.aanotes.data.DataStoreManager
import com.arton.aanotes.domain.repo.NotesRepository
import com.arton.aanotes.domain.room.DatabasePasswordManager
import com.arton.aanotes.domain.room.NotesDatabase
import com.arton.aanotes.domain.room.dao.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)


    @Provides
    @Singleton
    fun provideNotesRepository() = NotesRepository()

    @Provides
    @Singleton
    fun provideDatabasePasswordManager(context: Context) =
        DatabasePasswordManager(
            CryptoManager()
        )

    @Provides
    fun provideDatabase(
        applicationContext: Context,
        databasePasswordManager: DatabasePasswordManager
    ) = NotesDatabase.getInstance(applicationContext, databasePasswordManager)

    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao = database.notesDao()
}