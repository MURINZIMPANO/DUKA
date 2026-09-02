package com.duka.android.data.repository;

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
public final class AndroidBusinessRepository_Factory implements Factory<AndroidBusinessRepository> {
  private final Provider<BusinessDao> businessDaoProvider;

  public AndroidBusinessRepository_Factory(Provider<BusinessDao> businessDaoProvider) {
    this.businessDaoProvider = businessDaoProvider;
  }

  @Override
  public AndroidBusinessRepository get() {
    return newInstance(businessDaoProvider.get());
  }

  public static AndroidBusinessRepository_Factory create(
      Provider<BusinessDao> businessDaoProvider) {
    return new AndroidBusinessRepository_Factory(businessDaoProvider);
  }

  public static AndroidBusinessRepository newInstance(BusinessDao businessDao) {
    return new AndroidBusinessRepository(businessDao);
  }
}
