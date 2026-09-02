package com.duka.app.data.repository;

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
public final class PurchaseRepository_Factory implements Factory<PurchaseRepository> {
  private final Provider<PurchaseDao> purchaseDaoProvider;

  public PurchaseRepository_Factory(Provider<PurchaseDao> purchaseDaoProvider) {
    this.purchaseDaoProvider = purchaseDaoProvider;
  }

  @Override
  public PurchaseRepository get() {
    return newInstance(purchaseDaoProvider.get());
  }

  public static PurchaseRepository_Factory create(Provider<PurchaseDao> purchaseDaoProvider) {
    return new PurchaseRepository_Factory(purchaseDaoProvider);
  }

  public static PurchaseRepository newInstance(PurchaseDao purchaseDao) {
    return new PurchaseRepository(purchaseDao);
  }
}
