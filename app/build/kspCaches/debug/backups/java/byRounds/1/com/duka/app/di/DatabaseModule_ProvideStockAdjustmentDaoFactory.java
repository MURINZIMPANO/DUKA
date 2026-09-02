package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.StockAdjustmentDao;
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
public final class DatabaseModule_ProvideStockAdjustmentDaoFactory implements Factory<StockAdjustmentDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideStockAdjustmentDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StockAdjustmentDao get() {
    return provideStockAdjustmentDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideStockAdjustmentDaoFactory create(
      Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideStockAdjustmentDaoFactory(dbProvider);
  }

  public static StockAdjustmentDao provideStockAdjustmentDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStockAdjustmentDao(db));
  }
}
