package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.SaleDao;
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
public final class DatabaseModule_ProvideSaleDaoFactory implements Factory<SaleDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideSaleDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SaleDao get() {
    return provideSaleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideSaleDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideSaleDaoFactory(dbProvider);
  }

  public static SaleDao provideSaleDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSaleDao(db));
  }
}
