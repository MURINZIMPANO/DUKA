package com.duka.android.data.repository;

import com.duka.app.data.local.dao.PurchaseDao;
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
public final class AndroidPurchaseRepository_Factory implements Factory<AndroidPurchaseRepository> {
  private final Provider<PurchaseDao> purchaseDaoProvider;

  public AndroidPurchaseRepository_Factory(Provider<PurchaseDao> purchaseDaoProvider) {
    this.purchaseDaoProvider = purchaseDaoProvider;
  }

  @Override
  public AndroidPurchaseRepository get() {
    return newInstance(purchaseDaoProvider.get());
  }

  public static AndroidPurchaseRepository_Factory create(
      Provider<PurchaseDao> purchaseDaoProvider) {
    return new AndroidPurchaseRepository_Factory(purchaseDaoProvider);
  }

  public static AndroidPurchaseRepository newInstance(PurchaseDao purchaseDao) {
    return new AndroidPurchaseRepository(purchaseDao);
  }
}
