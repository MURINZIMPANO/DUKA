package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.ExpenseDao;
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
public final class DatabaseModule_ProvideExpenseDaoFactory implements Factory<ExpenseDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideExpenseDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ExpenseDao get() {
    return provideExpenseDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideExpenseDaoFactory create(Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideExpenseDaoFactory(dbProvider);
  }

  public static ExpenseDao provideExpenseDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExpenseDao(db));
  }
}
