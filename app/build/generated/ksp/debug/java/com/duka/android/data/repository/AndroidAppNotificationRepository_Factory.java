package com.duka.android.data.repository;

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
public final class AndroidAppNotificationRepository_Factory implements Factory<AndroidAppNotificationRepository> {
  private final Provider<AppNotificationDao> appNotificationDaoProvider;

  public AndroidAppNotificationRepository_Factory(
      Provider<AppNotificationDao> appNotificationDaoProvider) {
    this.appNotificationDaoProvider = appNotificationDaoProvider;
  }

  @Override
  public AndroidAppNotificationRepository get() {
    return newInstance(appNotificationDaoProvider.get());
  }

  public static AndroidAppNotificationRepository_Factory create(
      Provider<AppNotificationDao> appNotificationDaoProvider) {
    return new AndroidAppNotificationRepository_Factory(appNotificationDaoProvider);
  }

  public static AndroidAppNotificationRepository newInstance(
      AppNotificationDao appNotificationDao) {
    return new AndroidAppNotificationRepository(appNotificationDao);
  }
}
