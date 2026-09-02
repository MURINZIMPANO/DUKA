package com.duka.app.data.repository;

import com.duka.app.data.local.dao.BusinessDao;
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
public final class BusinessRepository_Factory implements Factory<BusinessRepository> {
  private final Provider<BusinessDao> businessDaoProvider;

  public BusinessRepository_Factory(Provider<BusinessDao> businessDaoProvider) {
    this.businessDaoProvider = businessDaoProvider;
  }

  @Override
  public BusinessRepository get() {
    return newInstance(businessDaoProvider.get());
  }

  public static BusinessRepository_Factory create(Provider<BusinessDao> businessDaoProvider) {
    return new BusinessRepository_Factory(businessDaoProvider);
  }

  public static BusinessRepository newInstance(BusinessDao businessDao) {
    return new BusinessRepository(businessDao);
  }
}
