package com.duka.android.data.repository;

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
public final class AndroidShopRatingRepository_Factory implements Factory<AndroidShopRatingRepository> {
  private final Provider<ShopRatingDao> shopRatingDaoProvider;

  public AndroidShopRatingRepository_Factory(Provider<ShopRatingDao> shopRatingDaoProvider) {
    this.shopRatingDaoProvider = shopRatingDaoProvider;
  }

  @Override
  public AndroidShopRatingRepository get() {
    return newInstance(shopRatingDaoProvider.get());
  }

  public static AndroidShopRatingRepository_Factory create(
      Provider<ShopRatingDao> shopRatingDaoProvider) {
    return new AndroidShopRatingRepository_Factory(shopRatingDaoProvider);
  }

  public static AndroidShopRatingRepository newInstance(ShopRatingDao shopRatingDao) {
    return new AndroidShopRatingRepository(shopRatingDao);
  }
}
