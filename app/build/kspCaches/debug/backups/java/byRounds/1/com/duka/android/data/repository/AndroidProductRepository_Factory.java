package com.duka.android.data.repository;

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
public final class AndroidProductRepository_Factory implements Factory<AndroidProductRepository> {
  private final Provider<ProductDao> productDaoProvider;

  public AndroidProductRepository_Factory(Provider<ProductDao> productDaoProvider) {
    this.productDaoProvider = productDaoProvider;
  }

  @Override
  public AndroidProductRepository get() {
    return newInstance(productDaoProvider.get());
  }

  public static AndroidProductRepository_Factory create(Provider<ProductDao> productDaoProvider) {
    return new AndroidProductRepository_Factory(productDaoProvider);
  }

  public static AndroidProductRepository newInstance(ProductDao productDao) {
    return new AndroidProductRepository(productDao);
  }
}
