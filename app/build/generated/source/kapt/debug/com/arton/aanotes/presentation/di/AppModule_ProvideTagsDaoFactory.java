package com.arton.aanotes.presentation.di;

import com.arton.aanotes.domain.room.NotesDatabase;
import com.arton.aanotes.domain.room.dao.TagsDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AppModule_ProvideTagsDaoFactory implements Factory<TagsDao> {
  private final Provider<NotesDatabase> databaseProvider;

  public AppModule_ProvideTagsDaoFactory(Provider<NotesDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TagsDao get() {
    return provideTagsDao(databaseProvider.get());
  }

  public static AppModule_ProvideTagsDaoFactory create(Provider<NotesDatabase> databaseProvider) {
    return new AppModule_ProvideTagsDaoFactory(databaseProvider);
  }

  public static TagsDao provideTagsDao(NotesDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTagsDao(database));
  }
}
