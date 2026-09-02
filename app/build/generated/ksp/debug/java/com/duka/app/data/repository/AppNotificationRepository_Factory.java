package com.duka.app.data.repository;

import com.duka.app.data.local.dao.AppNotificationDao;
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
public final class AppNotificationRepository_Factory implements Factory<AppNotificationRepository> {
  private final Provider<AppNotificationDao> appNotificationDaoProvider;

  public AppNotificationRepository_Factory(
      Provider<AppNotificationDao> appNotificationDaoProvider) {
    this.appNotificationDaoProvider = appNotificationDaoProvider;
  }

  @Override
  public AppNotificationRepository get() {
    return newInstance(appNotificationDaoProvider.get());
  }

  public static AppNotificationRepository_Factory create(
      Provider<AppNotificationDao> appNotificationDaoProvider) {
    return new AppNotificationRepository_Factory(appNotificationDaoProvider);
  }

  public static AppNotificationRepository newInstance(AppNotificationDao appNotificationDao) {
    return new AppNotificationRepository(appNotificationDao);
  }
}
