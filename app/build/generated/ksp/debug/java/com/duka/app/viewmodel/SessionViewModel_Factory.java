package com.duka.app.viewmodel;

import com.duka.app.data.repository.BusinessRepository;
import com.duka.app.data.repository.EmployeeRepository;
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
public final class SessionViewModel_Factory implements Factory<SessionViewModel> {
  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<BusinessRepository> businessRepositoryProvider;

  private final Provider<EmployeeRepository> employeeRepositoryProvider;

  public SessionViewModel_Factory(Provider<SessionManager> sessionManagerProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<EmployeeRepository> employeeRepositoryProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.businessRepositoryProvider = businessRepositoryProvider;
    this.employeeRepositoryProvider = employeeRepositoryProvider;
  }

  @Override
  public SessionViewModel get() {
    return newInstance(sessionManagerProvider.get(), userRepositoryProvider.get(), businessRepositoryProvider.get(), employeeRepositoryProvider.get());
  }

  public static SessionViewModel_Factory create(Provider<SessionManager> sessionManagerProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<EmployeeRepository> employeeRepositoryProvider) {
    return new SessionViewModel_Factory(sessionManagerProvider, userRepositoryProvider, businessRepositoryProvider, employeeRepositoryProvider);
  }

  public static SessionViewModel newInstance(SessionManager sessionManager,
      UserRepository userRepository, BusinessRepository businessRepository,
      EmployeeRepository employeeRepository) {
    return new SessionViewModel(sessionManager, userRepository, businessRepository, employeeRepository);
  }
}
