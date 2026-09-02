package com.duka.notifications;

import com.duka.app.data.session.SessionManager;
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
public final class NotificationPreferences_Factory implements Factory<NotificationPreferences> {
  private final Provider<SessionManager> sessionManagerProvider;

  public NotificationPreferences_Factory(Provider<SessionManager> sessionManagerProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public NotificationPreferences get() {
    return newInstance(sessionManagerProvider.get());
  }

  public static NotificationPreferences_Factory create(
      Provider<SessionManager> sessionManagerProvider) {
    return new NotificationPreferences_Factory(sessionManagerProvider);
  }

  public static NotificationPreferences newInstance(SessionManager sessionManager) {
    return new NotificationPreferences(sessionManager);
  }
}
