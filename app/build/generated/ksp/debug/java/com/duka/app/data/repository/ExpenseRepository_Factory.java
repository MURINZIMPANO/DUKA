package com.duka.app.data.repository;

import com.duka.app.data.local.dao.ExpenseDao;
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
public final class ExpenseRepository_Factory implements Factory<ExpenseRepository> {
  private final Provider<ExpenseDao> expenseDaoProvider;

  public ExpenseRepository_Factory(Provider<ExpenseDao> expenseDaoProvider) {
    this.expenseDaoProvider = expenseDaoProvider;
  }

  @Override
  public ExpenseRepository get() {
    return newInstance(expenseDaoProvider.get());
  }

  public static ExpenseRepository_Factory create(Provider<ExpenseDao> expenseDaoProvider) {
    return new ExpenseRepository_Factory(expenseDaoProvider);
  }

  public static ExpenseRepository newInstance(ExpenseDao expenseDao) {
    return new ExpenseRepository(expenseDao);
  }
}
