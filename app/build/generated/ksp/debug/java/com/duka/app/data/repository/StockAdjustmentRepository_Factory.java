package com.duka.app.data.repository;

import com.duka.app.data.local.dao.StockAdjustmentDao;
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
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class StockAdjustmentRepository_Factory implements Factory<StockAdjustmentRepository> {
  private final Provider<StockAdjustmentDao> stockAdjustmentDaoProvider;

  public StockAdjustmentRepository_Factory(
      Provider<StockAdjustmentDao> stockAdjustmentDaoProvider) {
    this.stockAdjustmentDaoProvider = stockAdjustmentDaoProvider;
  }

  @Override
  public StockAdjustmentRepository get() {
    return newInstance(stockAdjustmentDaoProvider.get());
  }

  public static StockAdjustmentRepository_Factory create(
      Provider<StockAdjustmentDao> stockAdjustmentDaoProvider) {
    return new StockAdjustmentRepository_Factory(stockAdjustmentDaoProvider);
  }

  public static StockAdjustmentRepository newInstance(StockAdjustmentDao stockAdjustmentDao) {
    return new StockAdjustmentRepository(stockAdjustmentDao);
  }
}
