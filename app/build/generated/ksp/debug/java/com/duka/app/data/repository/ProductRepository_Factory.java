package com.duka.app.data.repository;

import com.duka.app.data.local.dao.ProductDao;
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
public final class ProductRepository_Factory implements Factory<ProductRepository> {
  private final Provider<ProductDao> productDaoProvider;

  public ProductRepository_Factory(Provider<ProductDao> productDaoProvider) {
    this.productDaoProvider = productDaoProvider;
  }

  @Override
  public ProductRepository get() {
    return newInstance(productDaoProvider.get());
  }

  public static ProductRepository_Factory create(Provider<ProductDao> productDaoProvider) {
    return new ProductRepository_Factory(productDaoProvider);
  }

  public static ProductRepository newInstance(ProductDao productDao) {
    return new ProductRepository(productDao);
  }
}
