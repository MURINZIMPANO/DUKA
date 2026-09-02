package com.duka.app.di;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.duka.app.data.local.dao.UserDao;
import com.duka.app.data.session.SessionManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class SessionModule_ProvideSessionManagerFactory implements Factory<SessionManager> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private final Provider<UserDao> userDaoProvider;

  public SessionModule_ProvideSessionManagerFactory(
      Provider<DataStore<Preferences>> dataStoreProvider, Provider<UserDao> userDaoProvider) {
    this.dataStoreProvider = dataStoreProvider;
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public SessionManager get() {
    return provideSessionManager(dataStoreProvider.get(), userDaoProvider.get());
  }

  public static SessionModule_ProvideSessionManagerFactory create(
      Provider<DataStore<Preferences>> dataStoreProvider, Provider<UserDao> userDaoProvider) {
    return new SessionModule_ProvideSessionManagerFactory(dataStoreProvider, userDaoProvider);
  }

  public static SessionManager provideSessionManager(DataStore<Preferences> dataStore,
      UserDao userDao) {
    return Preconditions.checkNotNullFromProvides(SessionModule.INSTANCE.provideSessionManager(dataStore, userDao));
  }
}
