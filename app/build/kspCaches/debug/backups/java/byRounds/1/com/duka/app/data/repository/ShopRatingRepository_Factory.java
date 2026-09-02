package com.duka.app.data.repository;

import com.duka.app.data.local.dao.ShopRatingDao;
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
public final class ShopRatingRepository_Factory implements Factory<ShopRatingRepository> {
  private final Provider<ShopRatingDao> shopRatingDaoProvider;

  public ShopRatingRepository_Factory(Provider<ShopRatingDao> shopRatingDaoProvider) {
    this.shopRatingDaoProvider = shopRatingDaoProvider;
  }

  @Override
  public ShopRatingRepository get() {
    return newInstance(shopRatingDaoProvider.get());
  }

  public static ShopRatingRepository_Factory create(Provider<ShopRatingDao> shopRatingDaoProvider) {
    return new ShopRatingRepository_Factory(shopRatingDaoProvider);
  }

  public static ShopRatingRepository newInstance(ShopRatingDao shopRatingDao) {
    return new ShopRatingRepository(shopRatingDao);
  }
}
