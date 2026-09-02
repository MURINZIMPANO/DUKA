package com.duka.app.data.repository;

import com.duka.app.data.local.dao.ProductAlertDao;
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
public final class ProductAlertRepository_Factory implements Factory<ProductAlertRepository> {
  private final Provider<ProductAlertDao> productAlertDaoProvider;

  public ProductAlertRepository_Factory(Provider<ProductAlertDao> productAlertDaoProvider) {
    this.productAlertDaoProvider = productAlertDaoProvider;
  }

  @Override
  public ProductAlertRepository get() {
    return newInstance(productAlertDaoProvider.get());
  }

  public static ProductAlertRepository_Factory create(
      Provider<ProductAlertDao> productAlertDaoProvider) {
    return new ProductAlertRepository_Factory(productAlertDaoProvider);
  }

  public static ProductAlertRepository newInstance(ProductAlertDao productAlertDao) {
    return new ProductAlertRepository(productAlertDao);
  }
}
