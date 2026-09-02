package com.duka.app.data.repository;

import com.duka.app.data.local.dao.PromoDao;
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
public final class PromoRepository_Factory implements Factory<PromoRepository> {
  private final Provider<PromoDao> promoDaoProvider;

  public PromoRepository_Factory(Provider<PromoDao> promoDaoProvider) {
    this.promoDaoProvider = promoDaoProvider;
  }

  @Override
  public PromoRepository get() {
    return newInstance(promoDaoProvider.get());
  }

  public static PromoRepository_Factory create(Provider<PromoDao> promoDaoProvider) {
    return new PromoRepository_Factory(promoDaoProvider);
  }

  public static PromoRepository newInstance(PromoDao promoDao) {
    return new PromoRepository(promoDao);
  }
}
