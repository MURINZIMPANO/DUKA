package com.duka.android.data.repository;

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
public final class AndroidEmployeeRepository_Factory implements Factory<AndroidEmployeeRepository> {
  private final Provider<EmployeeDao> employeeDaoProvider;

  public AndroidEmployeeRepository_Factory(Provider<EmployeeDao> employeeDaoProvider) {
    this.employeeDaoProvider = employeeDaoProvider;
  }

  @Override
  public AndroidEmployeeRepository get() {
    return newInstance(employeeDaoProvider.get());
  }

  public static AndroidEmployeeRepository_Factory create(
      Provider<EmployeeDao> employeeDaoProvider) {
    return new AndroidEmployeeRepository_Factory(employeeDaoProvider);
  }

  public static AndroidEmployeeRepository newInstance(EmployeeDao employeeDao) {
    return new AndroidEmployeeRepository(employeeDao);
  }
}
