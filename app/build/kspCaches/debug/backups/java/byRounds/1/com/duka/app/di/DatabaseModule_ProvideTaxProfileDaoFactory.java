package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.TaxProfileDao;
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
public final class DatabaseModule_ProvideTaxProfileDaoFactory implements Factory<TaxProfileDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideTaxProfileDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TaxProfileDao get() {
    return provideTaxProfileDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTaxProfileDaoFactory create(
      Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideTaxProfileDaoFactory(dbProvider);
  }

  public static TaxProfileDao provideTaxProfileDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTaxProfileDao(db));
  }
}
