package com.duka.android.data.repository;

import com.duka.app.data.local.dao.BudgetGoalDao;
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
public final class AndroidBudgetGoalRepository_Factory implements Factory<AndroidBudgetGoalRepository> {
  private final Provider<BudgetGoalDao> budgetGoalDaoProvider;

  public AndroidBudgetGoalRepository_Factory(Provider<BudgetGoalDao> budgetGoalDaoProvider) {
    this.budgetGoalDaoProvider = budgetGoalDaoProvider;
  }

  @Override
  public AndroidBudgetGoalRepository get() {
    return newInstance(budgetGoalDaoProvider.get());
  }

  public static AndroidBudgetGoalRepository_Factory create(
      Provider<BudgetGoalDao> budgetGoalDaoProvider) {
    return new AndroidBudgetGoalRepository_Factory(budgetGoalDaoProvider);
  }

  public static AndroidBudgetGoalRepository newInstance(BudgetGoalDao budgetGoalDao) {
    return new AndroidBudgetGoalRepository(budgetGoalDao);
  }
}
