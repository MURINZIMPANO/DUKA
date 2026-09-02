package com.duka.settings.shared;

import android.app.Application;
import com.duka.app.data.repository.BusinessRepository;
import com.duka.app.data.repository.ChatRepository;
import com.duka.app.data.repository.UserRepository;
import com.duka.app.data.session.SessionManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<BusinessRepository> businessRepositoryProvider;

  private final Provider<ChatRepository> chatRepositoryProvider;

  public SettingsViewModel_Factory(Provider<Application> applicationProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider) {
    this.applicationProvider = applicationProvider;
    this.sessionManagerProvider = sessionManagerProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.businessRepositoryProvider = businessRepositoryProvider;
    this.chatRepositoryProvider = chatRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(applicationProvider.get(), sessionManagerProvider.get(), userRepositoryProvider.get(), businessRepositoryProvider.get(), chatRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider) {
    return new SettingsViewModel_Factory(applicationProvider, sessionManagerProvider, userRepositoryProvider, businessRepositoryProvider, chatRepositoryProvider);
  }

  public static SettingsViewModel newInstance(Application application,
      SessionManager sessionManager, UserRepository userRepository,
      BusinessRepository businessRepository, ChatRepository chatRepository) {
    return new SettingsViewModel(application, sessionManager, userRepository, businessRepository, chatRepository);
  }
}
