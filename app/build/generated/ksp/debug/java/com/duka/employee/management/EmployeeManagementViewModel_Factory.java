package com.duka.employee.management;

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
public final class EmployeeManagementViewModel_Factory implements Factory<EmployeeManagementViewModel> {
  private final Provider<EmployeeRepository> employeeRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<BusinessRepository> businessRepositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public EmployeeManagementViewModel_Factory(
      Provider<EmployeeRepository> employeeRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.employeeRepositoryProvider = employeeRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.businessRepositoryProvider = businessRepositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public EmployeeManagementViewModel get() {
    return newInstance(employeeRepositoryProvider.get(), userRepositoryProvider.get(), businessRepositoryProvider.get(), sessionManagerProvider.get());
  }

  public static EmployeeManagementViewModel_Factory create(
      Provider<EmployeeRepository> employeeRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new EmployeeManagementViewModel_Factory(employeeRepositoryProvider, userRepositoryProvider, businessRepositoryProvider, sessionManagerProvider);
  }

  public static EmployeeManagementViewModel newInstance(EmployeeRepository employeeRepository,
      UserRepository userRepository, BusinessRepository businessRepository,
      SessionManager sessionManager) {
    return new EmployeeManagementViewModel(employeeRepository, userRepository, businessRepository, sessionManager);
  }
}
