package com.duka.android.data.repository;

import com.duka.app.data.local.dao.UserDao;
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
public final class AndroidUserRepository_Factory implements Factory<AndroidUserRepository> {
  private final Provider<UserDao> userDaoProvider;

  public AndroidUserRepository_Factory(Provider<UserDao> userDaoProvider) {
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public AndroidUserRepository get() {
    return newInstance(userDaoProvider.get());
  }

  public static AndroidUserRepository_Factory create(Provider<UserDao> userDaoProvider) {
    return new AndroidUserRepository_Factory(userDaoProvider);
  }

  public static AndroidUserRepository newInstance(UserDao userDao) {
    return new AndroidUserRepository(userDao);
  }
}
