package com.duka.android.data.repository;

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
public final class AndroidPromoRepository_Factory implements Factory<AndroidPromoRepository> {
  private final Provider<PromoDao> promoDaoProvider;

  public AndroidPromoRepository_Factory(Provider<PromoDao> promoDaoProvider) {
    this.promoDaoProvider = promoDaoProvider;
  }

  @Override
  public AndroidPromoRepository get() {
    return newInstance(promoDaoProvider.get());
  }

  public static AndroidPromoRepository_Factory create(Provider<PromoDao> promoDaoProvider) {
    return new AndroidPromoRepository_Factory(promoDaoProvider);
  }

  public static AndroidPromoRepository newInstance(PromoDao promoDao) {
    return new AndroidPromoRepository(promoDao);
  }
}
