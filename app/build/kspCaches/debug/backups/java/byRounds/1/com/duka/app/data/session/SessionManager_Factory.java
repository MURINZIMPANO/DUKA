package com.duka.app.data.session;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class SessionManager_Factory implements Factory<SessionManager> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private final Provider<UserDao> userDaoProvider;

  public SessionManager_Factory(Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<UserDao> userDaoProvider) {
    this.dataStoreProvider = dataStoreProvider;
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public SessionManager get() {
    return newInstance(dataStoreProvider.get(), userDaoProvider.get());
  }

  public static SessionManager_Factory create(Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<UserDao> userDaoProvider) {
    return new SessionManager_Factory(dataStoreProvider, userDaoProvider);
  }

  public static SessionManager newInstance(DataStore<Preferences> dataStore, UserDao userDao) {
    return new SessionManager(dataStore, userDao);
  }
}
