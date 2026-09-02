package com.duka.app;

import com.duka.app.data.session.SessionManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DukaApplication_MembersInjector implements MembersInjector<DukaApplication> {
  private final Provider<DukaInitializer> initializerProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public DukaApplication_MembersInjector(Provider<DukaInitializer> initializerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.initializerProvider = initializerProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  public static MembersInjector<DukaApplication> create(
      Provider<DukaInitializer> initializerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new DukaApplication_MembersInjector(initializerProvider, sessionManagerProvider);
  }

  @Override
  public void injectMembers(DukaApplication instance) {
    injectInitializer(instance, initializerProvider.get());
    injectSessionManager(instance, sessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.duka.app.DukaApplication.initializer")
  public static void injectInitializer(DukaApplication instance, DukaInitializer initializer) {
    instance.initializer = initializer;
  }

  @InjectedFieldSignature("com.duka.app.DukaApplication.sessionManager")
  public static void injectSessionManager(DukaApplication instance, SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }
}
