package com.duka.android.data.repository;

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
public final class AndroidProductAlertRepository_Factory implements Factory<AndroidProductAlertRepository> {
  private final Provider<ProductAlertDao> productAlertDaoProvider;

  public AndroidProductAlertRepository_Factory(Provider<ProductAlertDao> productAlertDaoProvider) {
    this.productAlertDaoProvider = productAlertDaoProvider;
  }

  @Override
  public AndroidProductAlertRepository get() {
    return newInstance(productAlertDaoProvider.get());
  }

  public static AndroidProductAlertRepository_Factory create(
      Provider<ProductAlertDao> productAlertDaoProvider) {
    return new AndroidProductAlertRepository_Factory(productAlertDaoProvider);
  }

  public static AndroidProductAlertRepository newInstance(ProductAlertDao productAlertDao) {
    return new AndroidProductAlertRepository(productAlertDao);
  }
}
