package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.PromoDao;
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
public final class DatabaseModule_ProvidePromoDaoFactory implements Factory<PromoDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvidePromoDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PromoDao get() {
    return providePromoDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePromoDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvidePromoDaoFactory(dbProvider);
  }

  public static PromoDao providePromoDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePromoDao(db));
  }
}
