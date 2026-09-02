package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.ProductDao;
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
public final class DatabaseModule_ProvideProductDaoFactory implements Factory<ProductDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideProductDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProductDao get() {
    return provideProductDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideProductDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideProductDaoFactory(dbProvider);
  }

  public static ProductDao provideProductDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProductDao(db));
  }
}
