package com.duka.android.data.repository;

import com.duka.app.data.local.dao.SaleDao;
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
public final class AndroidSaleRepository_Factory implements Factory<AndroidSaleRepository> {
  private final Provider<SaleDao> saleDaoProvider;

  public AndroidSaleRepository_Factory(Provider<SaleDao> saleDaoProvider) {
    this.saleDaoProvider = saleDaoProvider;
  }

  @Override
  public AndroidSaleRepository get() {
    return newInstance(saleDaoProvider.get());
  }

  public static AndroidSaleRepository_Factory create(Provider<SaleDao> saleDaoProvider) {
    return new AndroidSaleRepository_Factory(saleDaoProvider);
  }

  public static AndroidSaleRepository newInstance(SaleDao saleDao) {
    return new AndroidSaleRepository(saleDao);
  }
}
