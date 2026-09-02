package com.duka.app.data.repository;

import com.duka.app.data.local.dao.EmployeeDao;
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
public final class EmployeeRepository_Factory implements Factory<EmployeeRepository> {
  private final Provider<EmployeeDao> employeeDaoProvider;

  public EmployeeRepository_Factory(Provider<EmployeeDao> employeeDaoProvider) {
    this.employeeDaoProvider = employeeDaoProvider;
  }

  @Override
  public EmployeeRepository get() {
    return newInstance(employeeDaoProvider.get());
  }

  public static EmployeeRepository_Factory create(Provider<EmployeeDao> employeeDaoProvider) {
    return new EmployeeRepository_Factory(employeeDaoProvider);
  }

  public static EmployeeRepository newInstance(EmployeeDao employeeDao) {
    return new EmployeeRepository(employeeDao);
  }
}
