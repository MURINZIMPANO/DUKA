package com.duka.android.data.repository;

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
public final class AndroidWholesalerRepository_Factory implements Factory<AndroidWholesalerRepository> {
  private final Provider<WholesalerDao> wholesalerDaoProvider;

  public AndroidWholesalerRepository_Factory(Provider<WholesalerDao> wholesalerDaoProvider) {
    this.wholesalerDaoProvider = wholesalerDaoProvider;
  }

  @Override
  public AndroidWholesalerRepository get() {
    return newInstance(wholesalerDaoProvider.get());
  }

  public static AndroidWholesalerRepository_Factory create(
      Provider<WholesalerDao> wholesalerDaoProvider) {
    return new AndroidWholesalerRepository_Factory(wholesalerDaoProvider);
  }

  public static AndroidWholesalerRepository newInstance(WholesalerDao wholesalerDao) {
    return new AndroidWholesalerRepository(wholesalerDao);
  }
}
