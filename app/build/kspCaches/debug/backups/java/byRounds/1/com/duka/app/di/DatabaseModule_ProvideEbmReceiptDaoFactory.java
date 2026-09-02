package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.EbmReceiptDao;
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
public final class DatabaseModule_ProvideEbmReceiptDaoFactory implements Factory<EbmReceiptDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideEbmReceiptDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EbmReceiptDao get() {
    return provideEbmReceiptDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEbmReceiptDaoFactory create(
      Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideEbmReceiptDaoFactory(dbProvider);
  }

  public static EbmReceiptDao provideEbmReceiptDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEbmReceiptDao(db));
  }
}
