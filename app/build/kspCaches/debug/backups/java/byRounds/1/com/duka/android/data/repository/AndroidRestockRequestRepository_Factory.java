package com.duka.android.data.repository;

import com.duka.app.data.local.dao.RestockRequestDao;
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
public final class AndroidRestockRequestRepository_Factory implements Factory<AndroidRestockRequestRepository> {
  private final Provider<RestockRequestDao> restockRequestDaoProvider;

  public AndroidRestockRequestRepository_Factory(
      Provider<RestockRequestDao> restockRequestDaoProvider) {
    this.restockRequestDaoProvider = restockRequestDaoProvider;
  }

  @Override
  public AndroidRestockRequestRepository get() {
    return newInstance(restockRequestDaoProvider.get());
  }

  public static AndroidRestockRequestRepository_Factory create(
      Provider<RestockRequestDao> restockRequestDaoProvider) {
    return new AndroidRestockRequestRepository_Factory(restockRequestDaoProvider);
  }

  public static AndroidRestockRequestRepository newInstance(RestockRequestDao restockRequestDao) {
    return new AndroidRestockRequestRepository(restockRequestDao);
  }
}
