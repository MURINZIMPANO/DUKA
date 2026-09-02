package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.EmployeeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideEmployeeDaoFactory implements Factory<EmployeeDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideEmployeeDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EmployeeDao get() {
    return provideEmployeeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEmployeeDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideEmployeeDaoFactory(dbProvider);
  }

  public static EmployeeDao provideEmployeeDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEmployeeDao(db));
  }
}
