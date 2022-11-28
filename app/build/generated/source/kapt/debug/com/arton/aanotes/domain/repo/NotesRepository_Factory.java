package com.arton.aanotes.domain.repo;

import com.arton.aanotes.domain.room.dao.NotesDao;
import com.arton.aanotes.domain.room.dao.TagsDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NotesRepository_Factory implements Factory<NotesRepository> {
  private final Provider<NotesDao> notesDaoProvider;

  private final Provider<TagsDao> tagsDaoProvider;

  public NotesRepository_Factory(Provider<NotesDao> notesDaoProvider,
      Provider<TagsDao> tagsDaoProvider) {
    this.notesDaoProvider = notesDaoProvider;
    this.tagsDaoProvider = tagsDaoProvider;
  }

  @Override
  public NotesRepository get() {
    return newInstance(notesDaoProvider.get(), tagsDaoProvider.get());
  }

  public static NotesRepository_Factory create(Provider<NotesDao> notesDaoProvider,
      Provider<TagsDao> tagsDaoProvider) {
    return new NotesRepository_Factory(notesDaoProvider, tagsDaoProvider);
  }

  public static NotesRepository newInstance(NotesDao notesDao, TagsDao tagsDao) {
    return new NotesRepository(notesDao, tagsDao);
  }
}
