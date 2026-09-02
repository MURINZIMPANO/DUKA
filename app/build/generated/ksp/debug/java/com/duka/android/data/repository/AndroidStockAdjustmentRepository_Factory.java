package com.duka.android.data.repository;

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
public final class AndroidStockAdjustmentRepository_Factory implements Factory<AndroidStockAdjustmentRepository> {
  private final Provider<StockAdjustmentDao> stockAdjustmentDaoProvider;

  public AndroidStockAdjustmentRepository_Factory(
      Provider<StockAdjustmentDao> stockAdjustmentDaoProvider) {
    this.stockAdjustmentDaoProvider = stockAdjustmentDaoProvider;
  }

  @Override
  public AndroidStockAdjustmentRepository get() {
    return newInstance(stockAdjustmentDaoProvider.get());
  }

  public static AndroidStockAdjustmentRepository_Factory create(
      Provider<StockAdjustmentDao> stockAdjustmentDaoProvider) {
    return new AndroidStockAdjustmentRepository_Factory(stockAdjustmentDaoProvider);
  }

  public static AndroidStockAdjustmentRepository newInstance(
      StockAdjustmentDao stockAdjustmentDao) {
    return new AndroidStockAdjustmentRepository(stockAdjustmentDao);
  }
}
