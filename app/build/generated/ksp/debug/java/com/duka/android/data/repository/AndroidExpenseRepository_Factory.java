package com.duka.android.data.repository;

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
public final class AndroidExpenseRepository_Factory implements Factory<AndroidExpenseRepository> {
  private final Provider<ExpenseDao> expenseDaoProvider;

  public AndroidExpenseRepository_Factory(Provider<ExpenseDao> expenseDaoProvider) {
    this.expenseDaoProvider = expenseDaoProvider;
  }

  @Override
  public AndroidExpenseRepository get() {
    return newInstance(expenseDaoProvider.get());
  }

  public static AndroidExpenseRepository_Factory create(Provider<ExpenseDao> expenseDaoProvider) {
    return new AndroidExpenseRepository_Factory(expenseDaoProvider);
  }

  public static AndroidExpenseRepository newInstance(ExpenseDao expenseDao) {
    return new AndroidExpenseRepository(expenseDao);
  }
}
