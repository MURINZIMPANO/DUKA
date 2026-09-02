package com.duka.app.data.repository;

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
public final class SaleRepository_Factory implements Factory<SaleRepository> {
  private final Provider<SaleDao> saleDaoProvider;

  public SaleRepository_Factory(Provider<SaleDao> saleDaoProvider) {
    this.saleDaoProvider = saleDaoProvider;
  }

  @Override
  public SaleRepository get() {
    return newInstance(saleDaoProvider.get());
  }

  public static SaleRepository_Factory create(Provider<SaleDao> saleDaoProvider) {
    return new SaleRepository_Factory(saleDaoProvider);
  }

  public static SaleRepository newInstance(SaleDao saleDao) {
    return new SaleRepository(saleDao);
  }
}
