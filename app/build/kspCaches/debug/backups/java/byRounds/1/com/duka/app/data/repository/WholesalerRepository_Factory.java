package com.duka.app.data.repository;

import com.duka.app.data.local.dao.WholesalerDao;
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
public final class WholesalerRepository_Factory implements Factory<WholesalerRepository> {
  private final Provider<WholesalerDao> wholesalerDaoProvider;

  public WholesalerRepository_Factory(Provider<WholesalerDao> wholesalerDaoProvider) {
    this.wholesalerDaoProvider = wholesalerDaoProvider;
  }

  @Override
  public WholesalerRepository get() {
    return newInstance(wholesalerDaoProvider.get());
  }

  public static WholesalerRepository_Factory create(Provider<WholesalerDao> wholesalerDaoProvider) {
    return new WholesalerRepository_Factory(wholesalerDaoProvider);
  }

  public static WholesalerRepository newInstance(WholesalerDao wholesalerDao) {
    return new WholesalerRepository(wholesalerDao);
  }
}
