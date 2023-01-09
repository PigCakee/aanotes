package com.arton.aanotes.domain.repo;

import com.arton.aanotes.data.DataStoreManager;
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

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  public NotesRepository_Factory(Provider<NotesDao> notesDaoProvider,
      Provider<TagsDao> tagsDaoProvider, Provider<DataStoreManager> dataStoreManagerProvider) {
    this.notesDaoProvider = notesDaoProvider;
    this.tagsDaoProvider = tagsDaoProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public NotesRepository get() {
    return newInstance(notesDaoProvider.get(), tagsDaoProvider.get(), dataStoreManagerProvider.get());
  }

  public static NotesRepository_Factory create(Provider<NotesDao> notesDaoProvider,
      Provider<TagsDao> tagsDaoProvider, Provider<DataStoreManager> dataStoreManagerProvider) {
    return new NotesRepository_Factory(notesDaoProvider, tagsDaoProvider, dataStoreManagerProvider);
  }

  public static NotesRepository newInstance(NotesDao notesDao, TagsDao tagsDao,
      DataStoreManager dataStoreManager) {
    return new NotesRepository(notesDao, tagsDao, dataStoreManager);
  }
}
