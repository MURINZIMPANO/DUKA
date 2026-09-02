package com.duka.intelligence;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.duka.app.data.local.dao.BusinessDao;
import com.duka.app.data.local.dao.ProductAlertDao;
import com.duka.app.data.local.dao.ProductDao;
import com.duka.app.data.local.dao.SaleDao;
import dagger.internal.DaggerGenerated;
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
public final class StockIntelligenceWorker_Factory {
  private final Provider<ProductDao> productDaoProvider;

  private final Provider<SaleDao> saleDaoProvider;

  private final Provider<BusinessDao> businessDaoProvider;

  private final Provider<ProductAlertDao> productAlertDaoProvider;

  public StockIntelligenceWorker_Factory(Provider<ProductDao> productDaoProvider,
      Provider<SaleDao> saleDaoProvider, Provider<BusinessDao> businessDaoProvider,
      Provider<ProductAlertDao> productAlertDaoProvider) {
    this.productDaoProvider = productDaoProvider;
    this.saleDaoProvider = saleDaoProvider;
    this.businessDaoProvider = businessDaoProvider;
    this.productAlertDaoProvider = productAlertDaoProvider;
  }

  public StockIntelligenceWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, productDaoProvider.get(), saleDaoProvider.get(), businessDaoProvider.get(), productAlertDaoProvider.get());
  }

  public static StockIntelligenceWorker_Factory create(Provider<ProductDao> productDaoProvider,
      Provider<SaleDao> saleDaoProvider, Provider<BusinessDao> businessDaoProvider,
      Provider<ProductAlertDao> productAlertDaoProvider) {
    return new StockIntelligenceWorker_Factory(productDaoProvider, saleDaoProvider, businessDaoProvider, productAlertDaoProvider);
  }

  public static StockIntelligenceWorker newInstance(Context context, WorkerParameters workerParams,
      ProductDao productDao, SaleDao saleDao, BusinessDao businessDao,
      ProductAlertDao productAlertDao) {
    return new StockIntelligenceWorker(context, workerParams, productDao, saleDao, businessDao, productAlertDao);
  }
}
