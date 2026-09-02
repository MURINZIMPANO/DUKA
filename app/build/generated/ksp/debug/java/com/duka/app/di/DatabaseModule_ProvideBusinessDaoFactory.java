package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.BusinessDao;
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
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideBusinessDaoFactory implements Factory<BusinessDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideBusinessDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BusinessDao get() {
    return provideBusinessDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideBusinessDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideBusinessDaoFactory(dbProvider);
  }

  public static BusinessDao provideBusinessDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBusinessDao(db));
  }
}
