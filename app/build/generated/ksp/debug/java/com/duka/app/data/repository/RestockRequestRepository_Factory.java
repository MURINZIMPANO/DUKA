package com.duka.app.data.repository;

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
public final class RestockRequestRepository_Factory implements Factory<RestockRequestRepository> {
  private final Provider<RestockRequestDao> restockRequestDaoProvider;

  public RestockRequestRepository_Factory(Provider<RestockRequestDao> restockRequestDaoProvider) {
    this.restockRequestDaoProvider = restockRequestDaoProvider;
  }

  @Override
  public RestockRequestRepository get() {
    return newInstance(restockRequestDaoProvider.get());
  }

  public static RestockRequestRepository_Factory create(
      Provider<RestockRequestDao> restockRequestDaoProvider) {
    return new RestockRequestRepository_Factory(restockRequestDaoProvider);
  }

  public static RestockRequestRepository newInstance(RestockRequestDao restockRequestDao) {
    return new RestockRequestRepository(restockRequestDao);
  }
}
